import tictactoe.StructureTicTacToe;
import alphabeta.Tree;
import alphabeta.TreeNode;
import tictactoe.TicTacToe_2D;
import tictactoe.TicTacToe_3D;
import utilities.Utilities;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
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
        lauchGame();

    }


    public static void lauchGame(){
        int dim = chooseDimension();
        StructureTicTacToe sttt = null;
        boolean ia;
        char firstToPlay;
        char player =' ';
        String response = "";
        while(!(response.equals("Y")) && !(response.equals("N")) ) {
            System.out.println("Voulez-vous commencer une nouvelle partie ou reprendre une partie ? (Y/N)");
            response = sc.nextLine();
            if (response.equals("Y")) {
                String file = "";
                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    file = chooser.getSelectedFile().getPath();


                }
                ArrayList<Integer> board = Utilities.parseBoard(dim, file);

                char [] tictactoe = Utilities.boardToTicTacToe(dim,board);
                if(dim == 2){
                    sttt = new TicTacToe_2D(tictactoe);
                }
                else{
                    sttt = new TicTacToe_3D(tictactoe);
                }

            }
            else if(response.equals("N")){
                if(dim == 2){
                    sttt = new TicTacToe_2D();
                }
                else{
                    sttt = new TicTacToe_3D();
                }
            }
        }
        while (true) {
            System.out.println("Voulez-vous jouez contre une IA (Y/N) ?");
            response = sc.nextLine();
            if(response.equals("Y") || response.equals("N")){
                ia = response.equals("Y");
                break;
            }
        }
        while (true) {
            System.out.println("Voulez-vous jouez les X ou les O ?");
            response = sc.nextLine();
            if(response.equals("X") || response.equals("O")){
                player = response.charAt(0);
                break;
            }
        }
        int size = sttt.getEmptyCell().size();
        firstToPlay = (size%2 == 0) ? 'X' : 'O';
        if(ia){
            turnIaPlayer(sttt,firstToPlay,player,dim);
        }
        else{
            turn2Player(sttt,firstToPlay,dim);
        }
    }

    public static void turn2Player(StructureTicTacToe sttt,char player,int dim){
        char player2 = (player == 'X') ? 'O' : 'X';
        while(true) {

            if(sttt.getEmptyCell().size() == 0) break;
            System.out.println("au Tour de " + player);
            int pos = playerPlay(dim);
            sttt.setCell(player, pos);
            while(!(sttt.getEmptyCell().contains(pos))){
                pos = playerPlay(dim);
            }
            System.out.println(sttt);
            if (sttt.findSolutionFromCell(pos)) {
                System.out.println("Joueur " + player + " gagne");
            }
            if(sttt.getEmptyCell().size() == 0) break;
            System.out.println("au Tour de " + player2);
            pos = playerPlay(dim);
            while(!(sttt.getEmptyCell().contains(pos))){
                pos = playerPlay(dim);
            }
            sttt.setCell(player2, pos);
            System.out.println(sttt);
            if (sttt.findSolutionFromCell(pos)) {
                System.out.println("Joueur " + player2 + " gagne");
            }
        }
        if(sttt.getEmptyCell().size() == 0) System.out.println("Draw");
    }

    public static void turnIaPlayer(StructureTicTacToe sttt,char firstToPlay,char humanPlayer,int dim){
        boolean iaBegin = firstToPlay != humanPlayer;
        char player2 = (humanPlayer == 'X') ? 'O' : 'X';
        while(true) {
            int pos;
            if(iaBegin){
                if(sttt.getEmptyCell().size() == 0) break;
                Tree t = new Tree(dim,player2,sttt);
                pos = t.nextStep();
                sttt.setCell(player2, pos);
                System.out.println(sttt);
                if (sttt.findSolutionFromCell(pos)) {
                    System.out.println("l'IA gagne");
                }
            }
            if(sttt.getEmptyCell().size() == 0) break;
            System.out.println("au Tour de " + humanPlayer);
            pos = playerPlay(dim);
            while(!(sttt.getEmptyCell().contains(pos))){
                pos = playerPlay(dim);
            }
            sttt.setCell(humanPlayer, pos);
            System.out.println(sttt);
            if (sttt.findSolutionFromCell(pos)) {
                System.out.println("Joueur " + humanPlayer + " gagne");
            }
            if(!iaBegin){
                if(sttt.getEmptyCell().size() == 0) break;
                Tree t = new Tree(dim,player2,sttt);
                pos = t.nextStep();
                sttt.setCell(player2, pos);
                System.out.println(sttt);
                if (sttt.findSolutionFromCell(pos)) {
                    System.out.println("l'IA gagne");
                }


            }
        }
        if(sttt.getEmptyCell().size() == 0) System.out.println("Draw");
    }

    public static int playerPlay(int dim){
        int line,column,depth;
        while(true) {
            System.out.print("Selectionnez votre ligne (1 à 4) :");
            try {
                line = Integer.parseInt(sc.nextLine());
                if (line >= 1 && line <= 4)
                    break;
                else
                    throw new NumberFormatException();
                // not positive.
            } catch (NumberFormatException e) {
                // not an integer
                System.out.print("pas un entier entre 1 et 4 ");
            }
        }
        while(true) {
            System.out.print("Selectionnez votre colonne (1 à 4) :");
            try {
                column = Integer.parseInt(sc.nextLine());
                if (column >= 1 && column <= 4)
                    break;
                else
                    throw new NumberFormatException();
                // not positive.
            } catch (NumberFormatException e) {
                // not an integer
                System.out.print("pas un entier entre 1 et 4 ");
            }
        }
        depth =1;
        if(dim == 3){
            while(true) {
                System.out.print("Selectionnez votre profondeur (1 à 4) :");
                try {
                    depth = Integer.parseInt(sc.nextLine());
                    if (depth >= 1 && depth <= 4)
                        break;
                    else
                        throw new NumberFormatException();
                    // not positive.
                } catch (NumberFormatException e) {
                    // not an integer
                    System.out.print("pas un entier entre 1 et 4 ");
                }
            }
        }
        return (depth-1)*16 + (line-1)*4 + column-1;


    }

    /**
     * Ask user to write dimension he wants to use (2 or 3).
     * @return Selected dimension.
     */
    public static int chooseDimension() {
        int dimension;
        while(true) {
            System.out.print("n-dimension (2 or 3): ");
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
