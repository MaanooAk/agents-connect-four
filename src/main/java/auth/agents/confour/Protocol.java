package auth.agents.confour;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class Protocol {

    public static void sendMove(AID aid, int move_x) {
        // TODO impl
    }

    public static void sendSeed(AID aid, int seed) {
        // TODO impl
    }

    public static int receiveMove(ACLMessage message) {
        // TODO impl
        return 0;
    }

    public static int receiveSeed(ACLMessage message) {
        // TODO impl
        return 0;
    }

}
