package utilities;

public class TicTacTocUtilities {

    public static String getCellPos(int cell) {
        int row, column, depth;
        depth = cell / 16;
        row = (cell - depth * 16) / 4;
        column = cell % 4;
        return String.format("%d,%d,%d", row, column, depth);
    }
}
