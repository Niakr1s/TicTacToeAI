package tictactoe;

import tictactoe.game.Game;
import tictactoe.game.config.Config;
import tictactoe.io.Helper;

public class Main {
    public static void main(String[] args) {
        Helper io = new Helper(System.in, System.out);
        Config config = io.getConfig();
        Game game = new Game(config);
        game.run(3);
    }
}
