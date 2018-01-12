package auth.agents.confour.game;

import java.util.Random;

public final class GameMaker {

    private GameMaker() {
    }

    /**
     * Create a seed used in the createGame
     *
     * @return the seed
     */
    public static int createRandomSeed() {
        return new Random().nextInt();
    }

    /**
     * Create a game based on two seeds
     *
     * @param seed1 the player's seed
     * @param seed2 the opponent's seed
     * @return return the new game
     */
    public static Game createGame(int seed1, int seed2) {

        return new Game(createFirst(seed1, seed2));
    }

    private static boolean createFirst(int seed1, int seed2) {

        Random ra = new Random(seed1 ^ seed2);

        return ra.nextBoolean() ^ seed1 > seed2;
    }

}
