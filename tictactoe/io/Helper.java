package tictactoe.io;

import tictactoe.board.Board;
import tictactoe.board.CanNotMoveException;
import tictactoe.game.config.Config;
import tictactoe.player.PlayerType;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class Helper {
    private final InputStream in;
    private final PrintStream out;

    public Helper(InputStream in, PrintStream out) {
        this.in = in;
        this.out = out;
    }

    public Scanner getScanner() {
        return new Scanner(in);
    }

    public Board getBoard() {
        out.print("Enter cells: ");
        String str = getNextNonEmptyLine();
        try {
            return new Board(str);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input");
            return getBoard();
        }
    }

    public Config getConfig() {
        final String badParameters = "Bad parameters!";
        while (true) {
            System.out.print("Enter command: ");
            String line = getNextNonEmptyLine();
            String[] splitted = line.split("\\s+");
            if (splitted.length != 3 || !Objects.equals(splitted[0],"start")) {
                out.println(badParameters);
                continue;
            }
            try {
                PlayerType firstPlayer = PlayerType.fromStr(splitted[1]);
                PlayerType secondPlayer = PlayerType.fromStr(splitted[2]);
                return new Config(firstPlayer, secondPlayer);
            } catch (IllegalArgumentException e) {
                System.out.println(badParameters);
            }
        }
    }

    public int getDimension() {
        while (true) {
            try {
                out.println("What dimension do you want?");
                String line = getNextNonEmptyLine();
                int dim = Integer.parseInt(line);
                if (dim < 2) {
                    out.println("Can't be less than 2");
                    continue;
                }
                return dim;
            } catch (NumberFormatException e) {
                out.println("Not a string");
            }
        }
    }

    public Board makeMove(Board board) {
        boolean madeMove = false;
        while (!madeMove) {
            try {
                out.print("Enter the coordinates: ");
                String line = getNextNonEmptyLine();
                board = board.makeMove(line);
                madeMove = true;
            } catch (CanNotMoveException | IllegalArgumentException e) {
                out.println(e.getMessage());
            }
        }
        return board;
    }

    public void printBoard(Board board) {
        Board.Printer printer = board.new Printer();
        printer.printBoard(out);
    }


    private String getNextNonEmptyLine() {
        Scanner scanner = getScanner();
        String res;
        do {
            res = scanner.nextLine();
        } while (res.isBlank());
        return res;
    }

    public void printStatus(Board.Status status) {
        out.println(status.getStr());
    }
}