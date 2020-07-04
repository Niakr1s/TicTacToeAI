package tictactoe.board;

import java.util.*;
import java.util.stream.Stream;

public class Cells implements Iterable<Cell> {
    private final List<Cell> cells = new ArrayList<>();

    public Cells() {

    }

    public Cells(Cell[] cells) {
        this.cells.addAll(List.of(cells));
    }

    public int size() {
        return cells.size();
    }

    public Stream<Cell> stream() {
        return this.cells.stream();
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    public Cells getEmptyCells() {
        Cells res = new Cells();
        for (Cell cell: this) {
            if (cell.getType() == null) res.addCell(cell);
        }
        return res;
    }

    public interface LinePredicate {
        boolean check(Cells line);
    }

    public Cells getEmptyCellsIf(LinePredicate predicate) {
        Cells res = new Cells();
        if (!predicate.check(this)) return res;
        if (!hasEmpty()) return res;
        return getEmptyCells();
    }

    public boolean hasEmpty() {
        return countOf(null) > 0;
    }

    public Cell get(int idx) {
        return this.cells.get(idx);
    }

    public int countOf(Cell.Type type) {
        int count = 0;
        for (Cell cell : this) {
            if (Objects.equals(cell.getType(), type)) count++;
        }
        return count;
    }

    @Override
    public Iterator<Cell> iterator() {
        return this.cells.iterator();
    }
}
