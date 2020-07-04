package tictactoe.board;

import java.util.ArrayList;
import java.util.List;

public class Pos {
    public final int row;
    public final int col;

    public Pos(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static List<Pos> generateAllPossiblePoses(int dimension) {
        List<Pos> res = new ArrayList<>(dimension * dimension);
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                res.add(new Pos(row, col));
            }
        }
        return res;
    }

    @Override
    public String toString() {
        return "Pos{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}