package tictactoe;

import java.util.ArrayList;

public class TicTacToe_3D extends StructureTicTacToe {

    public TicTacToe_3D() {
        super(64);
    }

    public TicTacToe_3D(char[] array) {
        super(array);
    }

    public TicTacToe_3D(char[] array, ArrayList<Integer> emptyCell) {
        super(array, emptyCell);
    }

    public TicTacToe_3D(TicTacToe_3D another) {
        super(another);
    }

    @Override
    public boolean solutionLine(int cell, char state) {
        return false;
    }

    @Override
    public boolean solutionColumn(int cell, char state) {
        return false;
    }

    @Override
    public boolean solutionDiagonal(int cell, char state) {
        return false;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int l = 1; l < 5; l++) {
            for (int d = 1; d < 5; d++) {
                for (int c = 1; c < 5; c++) {
                    if (c != 1) {
                        s.append("|");
                    }
                    s.append(this.getCell(l, c, d));
                }
                s.append("     ");
            }
            s.append(System.lineSeparator());
        }

        return s.toString();
    }
}
