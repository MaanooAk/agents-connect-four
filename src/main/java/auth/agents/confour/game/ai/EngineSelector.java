/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auth.agents.confour.game.ai;

/**
 *
 * @author Anastatsia
 */
public class EngineSelector {
    
    public static IEngine selectEngine(String engineName) {

        engineName = engineName.toLowerCase();

        if (engineName.equals("random")) {
            return new EngineRandom();
        } else if (engineName.startsWith("ab")) {

            Evaluator e;
            if (engineName.contains("quads")) e = new EvaluatorByTable();
            else if (engineName.contains("groupcount")) e = new EvaluatorByTable();
            else e = new EvaluatorByTable();

            int depth = 3;
            if (engineName.contains("#")) {
                depth = Integer.parseInt(engineName.substring(engineName.indexOf("#") + 1));
            }

            return new ABEngine(e, depth);
        } else {
            throw new RuntimeException("Engine name not found");
        }

    }
}
