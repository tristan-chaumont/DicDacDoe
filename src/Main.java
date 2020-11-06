import TicTacToe.TicTacToe_2D;

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
    /*enum CELL {
        BLANK,
        X,
        O
    }*/

    public static void main(String[] args) {

        System.out.print("n-dimension (2 or 3): ");
        int dimension;

        dimension = chooseDimension();

        ArrayList<Integer> ttt = new ArrayList<Integer>();

        // Read file
        try {
            BufferedReader reader = new BufferedReader(new FileReader("files/2D_Blank.txt"));
            String line;
            while((line = reader.readLine()) != null) {
                List<Integer> intLine = Arrays.stream(line.split("\s")).map(Integer::parseInt).collect(Collectors.toList());
                ttt.addAll(intLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        char [] tictactoe = new char[16];
        int i = 0;
        for(int cell : ttt){
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
