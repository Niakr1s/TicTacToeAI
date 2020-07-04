package tictactoe.board;

public class CanNotMoveException extends Exception {
    CanNotMoveException(String str) {
        super(str);
    }
}