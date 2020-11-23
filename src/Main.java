import tictactoe.StructureTicTacToe;
import alphabeta.Tree;
import alphabeta.TreeNode;
import tictactoe.TicTacToe_2D;
import tictactoe.TicTacToe_3D;
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

        ArrayList<Integer> board = Utilities.parseBoard(dimension, "files/3D/3D_Empty.txt");

        char [] tictactoe = new char[(int) Math.pow(4, dimension)];
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
        Tree tree = new Tree(2,'X');
        ArrayList<TreeNode> path = tree.getRoot().alphaBeta();
        for ( TreeNode tn: path ) {
            System.out.println(tn.getSituation());
        }
        // Test de Tristan
        //System.out.println(game);
        //StructureTicTacToe game = new TicTacToe_3D(tictactoe);
        //System.out.println(game);

        /*int j = 1;
        ArrayList<Integer> board3D = Utilities.parseBoard(3, "files/3D/3D_Empty.txt");
        for (Integer integer : board3D) {
            System.out.print(integer + " ");
            if(j==4) {
                System.out.println();
                j = 0;
            }
            j+=1;
        }*/
    }

    /**
     * Ask user to write dimension he wants to use (2 or 3).
     * @return Selected dimension.
     */
    public static int chooseDimension() {
        int dimension;
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
