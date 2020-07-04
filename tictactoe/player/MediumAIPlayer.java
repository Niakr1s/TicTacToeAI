package tictactoe.player;

import tictactoe.board.Board;
import tictactoe.board.CanNotMoveException;
import tictactoe.board.Cell;
import tictactoe.board.Cells;

public class MediumAIPlayer extends EasyAIPlayer implements Player {
    @Override
    public Board makeMove(Board board) {
        Board res;

        res = winIfWinnable(board);
        if (res != null) return res;

        res = preventOpponentWin(board);
        if (res != null) return res;

        return super.makeMove(board);
    }

    protected Board winIfWinnable(Board board) {
        for (Cells line : board) {
            try {
                Cell emptyCell = line.getEmptyCellsIf(
                        line1 -> line1.countOf(board.getCurrentTurn()) == line1.size() - 1).get(0);
                Board newBoard = board.makeMove(emptyCell.getPos());
                notifyMoveMade();
                return newBoard;
            } catch (CanNotMoveException | IndexOutOfBoundsException e) {
                continue;
            }
        }
        return null;
    }

    protected Board preventOpponentWin(Board board) {
        for (Cells line : board) {
            try {
                Cell emptyCell = line.getEmptyCellsIf(
                        line1 -> line1.countOf(board.getCurrentTurn().flipped()) == line1.size() - 1).get(0);
                Board newBoard = board.makeMove(emptyCell.getPos());
                notifyMoveMade();
                return newBoard;
            } catch (CanNotMoveException | IndexOutOfBoundsException e) {
                continue;
            }
        }
        return null;
    }

    @Override
    protected void notifyMoveMade() {
        System.out.println("Making move level \"medium\"");
    }
}
