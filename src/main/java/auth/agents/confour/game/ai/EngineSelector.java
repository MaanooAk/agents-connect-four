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
        switch (engineName.toLowerCase()) {
            case "random":
                return new EngineRandom();
            case "ab table":
                return new EngineAB(new EvaluatorByTable());
            case "ab quads":
                return new EngineAB(new EvaluatorByQuads());
            case "ab groupcount": 
                return new EngineAB(new EvaluatorByGroupCount());
            default :
                return new EngineRandom();
        }
    }
}