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
        descService.setType("player");

        descAgent.addServices(descService);

        try {
            DFService.register(agent, descAgent);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }

    }
    
    public void deregister() {

        try {
            DFService.deregister(agent);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
    }

    public AID find() {

        DFAgentDescription descAgent = new DFAgentDescription();

        ServiceDescription descService = new ServiceDescription();
        descService.setType("player");

        descAgent.addServices(descService);

        try {
            DFAgentDescription[] results = DFService.search(agent, descAgent);

            if (results.length == 0) return null;

            return results[0].getName();

        } catch (FIPAException e) {
            return null;
        }

    }

    public void requestGame(AID aid) {

        agent.send(newMessage(ACLMessage.PROPOSE, aid, "game?"));
    }

    public void acceptGame(AID aid) {

        agent.send(newMessage(ACLMessage.ACCEPT_PROPOSAL, aid, ""));
    }

    public void rejectGame(AID aid) {

        agent.send(newMessage(ACLMessage.REJECT_PROPOSAL, aid, ""));
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
