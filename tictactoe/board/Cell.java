package tictactoe.board;

public class Cell {
    private final Type type;
    private final Pos pos;

    Cell(Type type, Pos pos) {
        this.type = type;
        this.pos = pos;
    }

    public Type getType() {
        return type;
    }

    public Pos getPos() {
        return pos;
    }

    public enum Type {
        X('X'),
        O('O');

        private final char ch;

        private Type(char ch) {
            this.ch = ch;
        }

        public static Type fromCh(char ch) {
            for (Type c : Type.values()) {
                if (c.ch == ch) {
                    return c;
                }
            }
            return null;
        }

        public Type flipped() {
            if (this == Type.O) return Type.X;
            return Type.O;
        }

        public char getCh() {
            return ch;
        }

        @Override
        public String toString() {
            return "" + ch;
        }
    }

}
