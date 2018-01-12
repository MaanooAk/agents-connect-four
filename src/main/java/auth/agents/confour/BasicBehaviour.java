package auth.agents.confour;

import auth.agents.confour.game.Game;
import auth.agents.confour.game.GameMaker;
import auth.agents.confour.game.ai.EngineRandom;
import auth.agents.confour.game.ai.IEngine;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class BasicBehaviour extends Behaviour {

    private final Protocol protocol;

    private AID opponent;
    private Game game;
    private int seed;
    private IEngine engine;

    public BasicBehaviour(Agent a) {
        super(a);

        protocol = new Protocol(a);

        opponent = null;
        game = null;

        seed = GameMaker.createRandomSeed();
        engine = new EngineRandom(); // TODO choose an engine
    }

    @Override
    public void action() {
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

            // TODO find opp somehow
            opponent = null; // TODO add value

            protocol.sendSeed(opponent, seed);

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

            // TODO statistics
            // TODO terminate
            // TODO return true
        }

        return false;
    }

    @Override
    public boolean done() {
        return false;
    }
}
