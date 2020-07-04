package tictactoe.player;

import tictactoe.board.Board;
import tictactoe.board.CanNotMoveException;
import tictactoe.board.Cell;
import tictactoe.board.Pos;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class EasyAIPlayer implements Player {
    private final PrintStream out;

    public EasyAIPlayer() {
        this.out = System.out;
    }

    public EasyAIPlayer(PrintStream out) {
        this.out = out;
    }

    @Override
    public Board makeMove(Board board) {
        int dim = board.dimension();
        List<Pos> poses = Pos.generateAllPossiblePoses(dim);
        Collections.shuffle(poses);
        while (!poses.isEmpty()) {
            Pos pos = poses.remove(poses.size() - 1);
            Cell.Type cell = board.getCell(pos.row, pos.col);
            if (Objects.isNull(cell)) {
                try {
                    Board newBoard =  board.makeMove(pos.row, pos.col);
                    notifyMoveMade();
                    return newBoard;
                } catch (CanNotMoveException e) {
                    continue;
                }
            }
        }
        return null;
    }

    protected void notifyMoveMade() {
        out.println("Making move level \"easy\"");
    }
}
