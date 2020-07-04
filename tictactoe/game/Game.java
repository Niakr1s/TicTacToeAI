package tictactoe.game;

import tictactoe.board.Board;
import tictactoe.game.config.Config;
import tictactoe.player.PlayerFactory;
import tictactoe.player.PlayerType;
import tictactoe.io.Helper;
import tictactoe.player.Player;

public class Game {
    private final Helper io = new Helper(System.in, System.out);
    private final Player[] players = new Player[2];
    private int currentPlayer = 0;

    public Game(Config config) {
        PlayerType[] players = config.getPlayers();
        for (int i = 0; i < config.getPlayers().length; i++) {
            this.players[i] = PlayerFactory.fromType(players[i]);
        }
    }

    private Player getCurrentPlayer() {
        return players[currentPlayer++ % 2];
    }

    private Board moveWithCurrentPlayer(Board board) {
        return getCurrentPlayer().makeMove(board);
    }

    public void run() {
        run(3);
    }

    public void run(int dimension) {
        Board board = new Board(dimension);
        io.printBoard(board);
        Board.Status status = board.getStatus();
        while (status == Board.Status.NOT_FINISHED) {
            board = moveWithCurrentPlayer(board);
            status = board.getStatus();
            io.printBoard(board);
        }
        io.printStatus(status);
    }

    public void runOneMove() {
        Board board = io.getBoard();
        io.printBoard(board);
        board = moveWithCurrentPlayer(board);
        io.printBoard(board);
        io.printStatus(board.getStatus());
    }
}