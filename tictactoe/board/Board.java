package tictactoe.board;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Objects;

public class Board implements Iterable<Cells> {
    /**
     * Cell[row][col]
     */
    private final Cell.Type[][] field;
    private static Cell.Type startCell = Cell.Type.X;

    /**
     * Constructs Board with default dimension = 3;
     */
    public Board() {
        this(3);
    }

    /**
     * @param cellsStr is string of type "OOOXXX___"
     */
    public Board(String cellsStr) throws IllegalArgumentException {
        int dim = (int) Math.sqrt(cellsStr.length());
        if (dim * dim != cellsStr.length()) throw new IllegalArgumentException();

        // I can't do
        field = new Cell.Type[dim][dim];

        char[] chars = cellsStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            Cell.Type cell = Cell.Type.fromCh(chars[i]);
            setCell(i, cell);
        }
    }

    /**
     * Constructs Board of dimension;
     */
    public Board(int dimension) {
        field = new Cell.Type[dimension][dimension];
    }

    public Board(Board other) {
        this.field = other.field.clone();
        for (int row = 0; row < this.field.length; row++) {
            this.field[row] = other.field[row].clone();
        }
    }

    /**
     * @param str contains row and col in human-readable format, ie: col and row are in range [1,..dimension],
     *            col starting from left, row starting from bottom
     * @return copy of new board
     */
    public Board makeMove(String str) throws IllegalArgumentException, CanNotMoveException {
        Parser p = new Parser();
        Pos pos = p.parseMoveStr(str);
        return makeMove(pos.row, pos.col);
    }

    /**
     * @param row is in inner format, contains row in range [0,..dimension]
     * @param col is in inner format, contains row in range [0,..dimension]
     * @return copy of new board
     */
    public Board makeMove(int row, int col) throws CanNotMoveException {
        Cell.Type cell = getCell(row, col);
        if (cell != null) throw new CanNotMoveException("This cell is occupied! Choose another one!");
        Board res = new Board(this);
        res.setCell(row, col, getCurrentTurn());
        return res;
    }

    public Board makeMoveMust(Pos pos) {
        try {
            return makeMove(pos.row, pos.col);
        } catch (CanNotMoveException ignored) {
        }
        return null;
    }

    public Board makeMove(Pos pos) throws CanNotMoveException {
        return makeMove(pos.row, pos.col);
    }

    public Cell.Type getCurrentTurn() {
        StatusChecker checker = new StatusChecker();
        int x = checker.numOfX();
        int o = checker.numOfO();
        if (x == o) return startCell;
        return startCell.flipped();
    }

    public Status getStatus() {
        return new StatusChecker().getStatus();
    }

    private void setCell(int row, int col, Cell.Type cell) {
        field[row][col] = cell;
    }

    private void setCell(int pos, Cell.Type cell) {
        int row = pos / dimension();
        int col = pos % dimension();
        setCell(row, col, cell);
    }

    public Cell.Type getCell(int row, int col) {
        return field[row][col];
    }

    private Cell.Type getCell(int pos) {
        int row = pos / dimension();
        int col = pos % dimension();
        return getCell(row, col);
    }

    private void removeCell(int row, int col) {
        setCell(row, col, null);
    }

    public int size() {
        int sz = 0;
        for (Cell.Type[] cells : field) {
            sz += cells.length;
        }
        return sz;
    }

    public int dimension() {
        return field.length;
    }

    public Cells getCells() {
        Cells res = new Cells();
        for (int row = 0; row < field.length; row++) {
            for (int col = 0; col < field[row].length; col++) {
                res.addCell(new Cell(field[row][col], new Pos(row, col)));
            }
        }
        return res;
    }

    @Override
    public Iterator<Cells> iterator() {
        return new LinesIterator();
    }

    private interface CellArrayProcessor {
        void process(Cells cells);
    }

    private interface CellProcessor {
        void process(Cell.Type cell);
    }

    private static class MutWrapper<T> {
        public T wrapped;

        public MutWrapper(T toWrap) {
            wrapped = toWrap;
        }
    }

    private class StatusChecker {
        private Status getStatus() {
            if (isImpossible()) {
                return Status.IMPOSSIBLE;
            }
            if (getWinRowsAmount(Cell.Type.X) > 0) {
                return Status.X_WINS;
            }
            if (getWinRowsAmount(Cell.Type.O) > 0) {
                return Status.O_WINS;
            }
            if (hasEmptyCells()) {
                return Status.NOT_FINISHED;
            }
            return Status.DRAW;
        }

        private boolean isImpossible() {
            if (Math.abs(numOfX() - numOfO()) >= 2) {
                return true;
            }
            return getWinRowsAmount(Cell.Type.X) > 0 && getWinRowsAmount(Cell.Type.O) > 0;
        }

        private int getWinRowsAmount(Cell.Type of) {
            MutWrapper<Integer> res = new MutWrapper<>(0);
            CellArrayProcessor f = cells -> {
                for (Cell c : cells) {
                    if (c.getType() != of) return;
                }
                res.wrapped++;
            };
            forEachRow(f);
            return res.wrapped;
        }

        public int numOf(Cell.Type of) {
            MutWrapper<Integer> num = new MutWrapper<>(0);
            forEach(cell -> {
                if (cell == of) num.wrapped++;
            });
            return num.wrapped;
        }

        public int numOfX() {
            return numOf(Cell.Type.X);
        }

        public int numOfO() {
            return numOf(Cell.Type.O);
        }

        private void forEach(CellProcessor f) {
            for (Cell.Type[] cells : field) {
                for (Cell.Type cell : cells) {
                    f.process(cell);
                }
            }
        }

        private void forEachRow(CellArrayProcessor f) {
            LinesIterator iterator = new LinesIterator();
            while (iterator.hasNext()) {
                Cells row = iterator.next();
                f.process(row);
            }
        }

        private boolean hasEmptyCells() {
            for (int i = 0; i < size(); i++) {
                if (getCell(i) == null) return true;
            }
            return false;
        }
    }

    public class Printer {
        public void printBoard(PrintStream out) {
            printDelimiter(out);
            for (int row = 0; row < field.length; row++) {
                printRow(row, out);
            }
            printDelimiter(out);
        }

        private void printDelimiter(PrintStream out) {
            int len = dimension() * 2 + 3;
            out.println("-".repeat(len));
        }

        private void printRow(int row, PrintStream out) {
            out.print("| ");
            for (int col = 0; col < field[row].length; col++) {
                Cell.Type cell = field[row][col];

                if (Objects.isNull(cell)) out.print("_");
                else out.print(cell.getCh());

                out.print(" ");
            }
            out.println("|");
        }
    }

    private class Parser {
        /**
         * @param str of a human-readable format (col, row)
         */
        private Pos parseMoveStr(String str) throws IllegalArgumentException {
            String[] splitted = str.split("\\s+");
            int col, row;
            try {
                col = Integer.parseInt(splitted[0]);
                row = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("You should enter numbers!");
            }
            // converting to inner coordinates
            col--;
            row--;
            if (col < 0 || col >= dimension() || row < 0 || row >= dimension()) {
                throw new IllegalArgumentException(String.format("Coordinates should be from 1 to %d!", dimension()));
            }
            row = dimension() - row - 1;
            return new Pos(row, col);
        }
    }

    public class LinesIterator implements Iterator<Cells> {
        private final Iterator<Cells>[] iterators;

        public LinesIterator() {
            this.iterators = new Iterator[]{
                    new ColIterator(),
                    new RowIterator(),
                    new DiagonalIterator()
            };
        }

        @Override
        public Cells next() {
            for (Iterator<Cells> iterator : iterators) {
                if (iterator.hasNext()) {
                    return iterator.next();
                }
            }
            return null;
        }

        @Override
        public boolean hasNext() {
            for (Iterator<Cells> iterator : iterators) {
                if (iterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        private class RowIterator implements Iterator<Cells> {
            private int row = 0;

            public boolean hasNext() {
                return row < dimension();
            }

            public Cells next() {
                Cell[] line = new Cell[dimension()];
                for (int col = 0; col < field[row].length; col++) {
                    line[col] = new Cell(field[row][col], new Pos(row, col));
                }
                row++;
                return new Cells(line);
            }
        }

        private class ColIterator implements Iterator<Cells> {
            private int col = 0;

            @Override
            public boolean hasNext() {
                return col < dimension();
            }

            @Override
            public Cells next() {
                Cell[] line = new Cell[dimension()];
                for (int row = 0; row < field.length; row++) {
                    line[row] = new Cell(field[row][col], new Pos(row, col));
                }
                col++;
                return new Cells(line);
            }
        }

        private class DiagonalIterator implements Iterator<Cells> {
            private int idx = -1;

            @Override
            public boolean hasNext() {
                return idx < 1;
            }

            @Override
            public Cells next() {
                idx++;
                if (idx == 0) return generateLeftDiagonal();
                if (idx == 1) return generateRightDiagonal();
                return null;
            }

            private Cells generateLeftDiagonal() {
                Cell[] line = new Cell[dimension()];
                for (int i = 0; i < dimension(); i++) {
                    line[i] = new Cell(field[i][i], new Pos(i, i));
                }
                return new Cells(line);
            }

            private Cells generateRightDiagonal() {
                Cell[] line = new Cell[dimension()];
                for (int i = 0; i < dimension(); i++) {
                    int rightCol = field[i].length - i - 1;
                    line[i] = new Cell(field[i][rightCol], new Pos(i, rightCol));
                }
                return new Cells(line);
            }
        }
    }

    public enum Status {
        NOT_FINISHED("Game not finished"),
        DRAW("Draw"),
        X_WINS("X wins"),
        O_WINS("O wins"),
        IMPOSSIBLE("Impossible");

        private String str;

        Status(String str) {
            this.str = str;
        }

        public String getStr() {
            return str;
        }
    }
}

