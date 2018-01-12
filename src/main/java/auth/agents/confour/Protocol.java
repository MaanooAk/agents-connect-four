package auth.agents.confour;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class Protocol {

    private final Agent agent;

    public Protocol(Agent agent) {
        this.agent = agent;
    }

    public void register() {
        
    }
    
    public void unregister() {
        
    }

    // ===

    public void sendMove(AID aid, int move_x) {
        // TODO impl
    }

    public void sendSeed(AID aid, int seed) {
        // TODO impl
    }

    public int receiveMove(ACLMessage message) {
        // TODO impl
        return 0;
    }

    public int receiveSeed(ACLMessage message) {
        // TODO impl
        return 0;
    }

}
