package auth.agents.confour;

import java.util.concurrent.Callable;

public final class AgentLogger {

    private AgentLogger() {}

    private static final boolean info = true;
    private static final boolean debug = false;

    public static void info(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    public static void info(Callable<String> text) {
        if (info) try {
            System.out.println(text.call());
        } catch (Exception e) { }
    }

    public static void debug(Callable<String> text) {
        if (debug) try {
            System.out.println(text.call());
        } catch (Exception e) { }
    }

}
