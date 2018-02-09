package auth.agents.confour;

import auth.agents.confour.game.Game;
import auth.agents.confour.game.GameMaker;
import auth.agents.confour.game.Statistics;
import auth.agents.confour.game.ai.IEngine;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class BasicBehaviour extends Behaviour {

    private static final int GAMES = 20;

    private final Protocol protocol;

    private AID opponent;
    private Game game;
    private int seed;
    private IEngine engine;
    private Statistics statistics;

    public BasicBehaviour(Agent a, IEngine engine) {
        super(a);
        this.engine = engine;

        protocol = new Protocol(a);

        opponent = null;
        game = null;

        seed = GameMaker.createRandomSeed();
        statistics = new Statistics();
    }

    @Override
    public void action() {

        tryFindOpp();

        while (true) {

            ACLMessage message = myAgent.receive();
            if (message != null) {

                if (handleMessage(message)) return;

            } else {
                block();
            }
        }
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

            System.out.println("Game created"); //debug

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
            
            if (statistics.getGames() >= GAMES) {
                System.out.println(getAgent().getName() + " finished with a win rate of " + statistics.getWinRate());
                return true;
            }
            
            System.out.println(getAgent().getName() + " finished game " + statistics.getGames());
            
            game = null;
            tryFindOpp();
        }

        return false;
    }

    public void tryFindOpp() {
    	System.out.println(getAgent().getName() + " looking for opp");

        opponent = null;
        
	    try {
	        // randomize the start order
	        Thread.sleep((long) (Math.random() * 500));
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
        return statistics.getGames() >= GAMES;
    }
}
