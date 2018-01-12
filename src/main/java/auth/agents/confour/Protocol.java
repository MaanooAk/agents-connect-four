package auth.agents.confour;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class Protocol {

    public static final String LANG = "English";
    public static final String ONTOLOGY = "connect-four-ontology";

    private final Agent agent;

    public Protocol(Agent agent) {
        this.agent = agent;
    }

    public void register() {

        DFAgentDescription descAgent = new DFAgentDescription();
        descAgent.setName(agent.getAID());

        ServiceDescription descService = new ServiceDescription();
        descService.setName("player");

        descAgent.addServices(descService);

        try {
            DFService.register(agent, descAgent);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }

    }
    
    public void unregister() {

        try {
            DFService.deregister(agent);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
    }

    // ===

    public void sendMove(AID aid, int move) {

        String content = Integer.toString(move);

        agent.send(newMessage(ACLMessage.INFORM, aid, content));
    }

    public void sendSeed(AID aid, int seed) {

        String content = Integer.toString(seed);

        agent.send(newMessage(ACLMessage.INFORM, aid, content));
    }

    public int receiveMove(ACLMessage message) {

        if(!message.getOntology().equals(ONTOLOGY)) throw new RuntimeException("Unknown ontology");

        int move = Integer.parseInt(message.getContent());

        return move;
    }

    public int receiveSeed(ACLMessage message) {

        if(!message.getOntology().equals(ONTOLOGY)) throw new RuntimeException("Unknown ontology");

        int seed = Integer.parseInt(message.getContent());

        return seed;
    }

    // ===

    private ACLMessage newMessage(int per, AID aid, String content) {

        ACLMessage message = new ACLMessage(per);
        message.addReceiver(aid);
        message.setLanguage(LANG);
        message.setOntology(ONTOLOGY);
        message.setContent(content);

        return message;
    }

}
