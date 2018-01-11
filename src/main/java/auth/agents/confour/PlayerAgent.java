package auth.agents.confour;

import jade.core.Agent;

public class PlayerAgent extends Agent {

    @Override
    public void setup() {

        System.out.println("PlayerAgent: setup");

        addBehaviour(new BasicBehaviour(this));
    }

    @Override
    public void takeDown() {

    }

}
