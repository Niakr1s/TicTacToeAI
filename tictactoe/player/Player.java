package tictactoe.player;

import tictactoe.board.Board;

public interface Player {
    Board makeMove(Board board);
}
