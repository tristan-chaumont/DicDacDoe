package tictactoe;

import utilities.Utilities;

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
        // trouve la case du debut de la ligne
        int begin = cell - cell%4;
        // parcours de la ligne
        for(int i = begin;i<begin+4;i++){
            if (this.cells[i] != state){
                clearWinningCells();
                return false;
            }
            setWinningCell(i);
        }
        return true;
    }

    @Override
    public boolean solutionColumn(int cell, char state) {
        //haut de la colonne
        int begin = cell%4;
        //parcours de la colonne
        for(int i = begin;i<16;i=i+4){
            if (this.cells[i] != state) {
                clearWinningCells();
                return false;
            }
            setWinningCell(i);
        }
        return true;
    }

    @Override
    public boolean solutionDiagonal(int cell, char state) {
        //verifie si il fait partie de la diagonale haut gauche a bas droite
        if(cell%5 == 0){
            //parcours de cette diagonale
            for(int i =0;i<16;i=i+5){
                if (this.cells[i] != state){
                    clearWinningCells();
                    return false;
                }
                setWinningCell(i);
            }
            return true;
        }
        //verifie si il fait partie de la diagonale haut droite a bas gauche
        else if(cell%3 == 0){
            //parcours de cette diagonale
            for(int i =3;i<13;i=i+3){
                if (this.cells[i] != state){
                    clearWinningCells();
                    return false;
                }
                setWinningCell(i);
            }
            return true;
        }
        //si il ne fait pas partie d'une de ces 2 diagonales retoune zero
        return false;
    }

    @Override
    public char getCell(int line,int column, int depth){
        return super.getCell(line, column, depth);
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
            s.append(Utilities.LINE_SEPARATOR);
        }

        return s.toString();
    }
}
