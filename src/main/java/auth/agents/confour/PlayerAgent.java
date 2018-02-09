package auth.agents.confour;

import auth.agents.confour.game.ai.EngineSelector;
import auth.agents.confour.game.ai.IEngine;
import jade.core.Agent;

public class PlayerAgent extends Agent {

    private static final String DEFAULT_ENGINE_NAME = "random";

    private String engineName = DEFAULT_ENGINE_NAME;

    @Override
    public void setup() {

        if (getArguments().length == 1) {
            engineName = (String) getArguments()[0];
        }

        AgentLogger.info("%s: Engine '%s'", getLocalName(), engineName);

        addBehaviour(new BasicBehaviour(this, EngineSelector.selectEngine(engineName)));
    }

    @Override
    public void takeDown() {

    }

}
