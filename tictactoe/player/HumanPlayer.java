package tictactoe.player;

import tictactoe.board.Board;
import tictactoe.io.Helper;

import java.io.InputStream;
import java.io.PrintStream;

public class HumanPlayer implements Player {
    private final Helper io;

    HumanPlayer() {
        this.io = new Helper(System.in, System.out);
    }

    HumanPlayer(InputStream in, PrintStream out) {
        this.io = new Helper(in, out);
    }

    @Override
    public Board makeMove(Board board) {
        return io.makeMove(board);
    }
}
