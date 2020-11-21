import tictactoe.TicTacToe_2D;
import utilities.Utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("n-dimension (2 or 3): ");
        int dimension;

        dimension = chooseDimension();

        ArrayList<Integer> board = Utilities.parseBoard(2, "files/2D/2D_Empty.txt");

        char [] tictactoe = new char[16];
        int i = 0;
        for(int cell : board){
            switch (cell) {
                case -1:
                    tictactoe[i] = ' ';
                    break;
                case 0:
                    tictactoe[i] = 'O';
                    break;
                case 1:
                    tictactoe[i] = 'X';
                    break;
            }
            i++;
        }
        TicTacToe_2D game = new TicTacToe_2D(tictactoe);
        System.out.println(game);
    }

    /**
     * Ask user to write dimension he wants to use (2 or 3).
     * @return Selected dimension.
     */
    public static int chooseDimension() {
        int dimension = 2;
        while(true) {
            try {
                dimension = Integer.parseInt(sc.nextLine());
                if (dimension == 2 || dimension == 3)
                    break;
                else
                    throw new NumberFormatException();
                // not positive.
            } catch (NumberFormatException e) {
                // not an integer
                System.out.print("n-dimension (2 or 3): ");
            }
        }
        return dimension;
    }
}
