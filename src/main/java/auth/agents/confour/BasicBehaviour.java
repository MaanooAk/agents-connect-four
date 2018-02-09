package auth.agents.confour;

import auth.agents.confour.game.Game;
import auth.agents.confour.game.GameMaker;
import auth.agents.confour.game.Statistics;
import auth.agents.confour.game.ai.IEngine;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.time.Duration;

public class BasicBehaviour extends Behaviour {

    private static final long LIFETIME = Duration.ofSeconds(30).toMillis();

    private final Protocol protocol;
    private final IEngine engine;
    private final Statistics statistics;

    private AID opponent;
    private Game game;
    private int seed;

    public BasicBehaviour(Agent a, IEngine engine) {
        super(a);
        this.engine = engine;

        protocol = new Protocol(a);
        statistics = new Statistics();

        opponent = null;
        game = null;

        seed = GameMaker.createRandomSeed();
    }

    @Override
    public void action() {

        tryFindOpp();

        while (statistics.getDuration() < LIFETIME) {

            ACLMessage message = myAgent.receive();
            if (message != null) {

                if (handleMessage(message)) return;

            } else {
                block(500); // to end registered agents on the lifetime limit
            }
        }

        AgentLogger.info("%s: %s", getAgent().getLocalName(), statistics);
    }

    private boolean handleMessage(ACLMessage message) {

        if (opponent == null) {

            if (message.getPerformative() == ACLMessage.REJECT_PROPOSAL) {

                tryFindOpp();

            } else if (message.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {

                opponent = message.getSender();

            } else if (message.getPerformative() == ACLMessage.PROPOSE) {

                opponent = message.getSender();
                protocol.acceptGame(opponent);

                protocol.deregister();
            }

            if (opponent != null) {

                protocol.sendSeed(opponent, seed);
            }

        } else if (opponent != null && !opponent.equals(message.getSender())) {

            protocol.rejectGame(message.getSender());

        } else if (game == null) {

            int otherSeed = protocol.receiveSeed(message);
            game = GameMaker.createGame(seed, otherSeed);

            AgentLogger.debug(() -> "Game created (" + myAgent.getLocalName() + " " + opponent.getLocalName() + ")");

        } else if (game != null){

            int move = protocol.receiveMove(message);
            game.addOpponentDisk(move);

        }

        if (game != null && game.isPlaying()) {

            int move = game.addPlayerDisk(engine);
            protocol.sendMove(opponent, move);
        }

        if (game != null && game.isOver()) {
            
            statistics.add(game);
            
            if (statistics.getDuration() >= LIFETIME) {
                return true;
            }

            AgentLogger.debug(() -> getAgent().getLocalName() + " finished " + statistics.getGames() + " games");

            tryFindOpp();
        }

        return false;
    }

    public void tryFindOpp() {
        AgentLogger.debug(() -> getAgent().getLocalName() + " looking for opp");

        game = null;
        opponent = null;
        
	    try {
	        // randomize the start order
	        Thread.sleep((long) (Math.random() * 50));
	    } catch (InterruptedException e) { }

        AID opp = protocol.find();
        if (opp != null) {
            protocol.requestGame(opp);
        } else {
            protocol.register();
        }
    }

    @Override
    public boolean done() {
        return statistics.getDuration() >= LIFETIME;
    }
}
