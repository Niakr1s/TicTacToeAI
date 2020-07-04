package tictactoe.player;

import tictactoe.board.*;

import java.util.*;

public class HardAIPlayer implements Player {
    private final Random rand = new Random();

    @Override
    public Board makeMove(Board board) {
        notifyMoveMade();
        return board.makeMoveMust(chooseBestPos(board));
    }

    private Pos chooseBestPos(Board board) {
        List<PosWithWeight> poses = new ArrayList<>();
        Cells emptyCells = board.getCells().getEmptyCells();
        for (Cell cell : emptyCells) {
            int weight = getWeight(cell.getPos(), board, board.getCurrentTurn());
            poses.add(new PosWithWeight(cell.getPos(), weight));
        }
        int maxWeight = Collections.max(poses, Comparator.comparingInt(o -> o.weight)).weight;
        poses.removeIf(posWithWeight -> posWithWeight.weight != maxWeight);
        return poses.get(rand.nextInt(poses.size())).pos;
    }

    /**
     * If win - returns 1, if draw - returns 0, if lose - returns -1.
     * If unfinished - tries to make move instead opponent.
     *
     * @param selfType self cell type
     */
    private int getWeight(Pos pos, Board board, Cell.Type selfType) {
        board = board.makeMoveMust(pos);

        try {
            return getWeight(board.getStatus(), selfType);
        } catch (Unfinished ignore) {
        }

        Cells emptyCells = board.getCells().getEmptyCells();
        final Set<Integer> weights = new HashSet<>();
        for (Cell emptyCell : emptyCells) {
            int w = getWeight(emptyCell.getPos(), board, selfType);

            // small speed up
            if (board.getCurrentTurn() == selfType && w == 1) return 1;
            else if (board.getCurrentTurn() != selfType && w == -1) return -1;

            weights.add(w);
        }
        // if our turn - choosing best situation for us, if not our - choosing worst situation for opponent.
        return board.getCurrentTurn() == selfType
                ? Collections.max(weights)
                : Collections.min(weights);
    }

    private int getWeight(Board.Status status, Cell.Type selfType) throws Unfinished {
        if (status == Board.Status.DRAW) return 0;
        if (status == Board.Status.X_WINS || status == Board.Status.O_WINS) {
            if (status == Board.Status.X_WINS && selfType == Cell.Type.X ||
                    status == Board.Status.O_WINS && selfType == Cell.Type.O) {
                return 1;
            }
            return -1;
        }
        throw new Unfinished();
    }

    protected void notifyMoveMade() {
        System.out.println("Making move level \"hard\"");
    }

    private static class Unfinished extends Exception {
    }

    private static class PosWithWeight {
        public final Pos pos;
        public final int weight;

        public PosWithWeight(Pos pos, int weight) {
            this.pos = pos;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "PosWithWeight{" +
                    "pos=" + pos +
                    ", weight=" + weight +
                    '}';
        }
    }
}
