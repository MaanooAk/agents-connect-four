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

        AID opp = protocol.find();
        if (opp != null) {
            protocol.requestGame(opp);
        }

        while (true) {

            ACLMessage message = myAgent.receive();
            if (message != null) {

                handleMessage(message);

            } else {
                block();
            }
        }
    }

    private boolean handleMessage(ACLMessage message) {

        if (opponent == null) {

            if (message.getPerformative() == ACLMessage.REJECT_PROPOSAL) {

                AID opp = protocol.find();
                if (opp != null) {
                    protocol.requestGame(opp);
                }

            } else if (message.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {

                opponent = message.getSender();

            } else if (message.getPerformative() == ACLMessage.PROPOSE) {

                opponent = message.getSender();
                protocol.acceptGame(opponent);
            }

            if (opponent != null) {

                protocol.sendSeed(opponent, seed);
                protocol.deregister();
            }

        } else if (opponent != message.getSender()) {

            protocol.rejectGame(message.getSender());

        } else if (game == null) {

            int otherSeed = protocol.receiveSeed(message);
            game = GameMaker.createGame(seed, otherSeed);

        } else if (game != null){

            int move = protocol.receiveMove(message);
            game.addOpponentDisk(move);

        }

        if (game != null && game.isPlaying()) {

            int move = game.addPlayerDisk(engine);
            protocol.sendMove(opponent, move);
        }

        if (game != null && game.isOver()) {
            
            statistics.add(game.isPlayerWinner(),game.numberOfMoves());

            if (statistics.getGames() >= GAMES) {
                System.out.println(getAgent().getName() + " finished with a win rate of " + statistics.getWinRate());
                return true;
            }
        }

        if (opponent == null) {
            protocol.register();
        }

        return false;
    }

    @Override
    public boolean done() {
        return statistics.getGames() >= GAMES;
    }
}
