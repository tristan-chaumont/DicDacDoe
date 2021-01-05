package tictactoe;

import utilities.Utilities;

import java.util.ArrayList;

public class TicTacToe_2D extends StructureTicTacToe {

    private static int[]  heuristic = {0,1,3,7,9999};

    public TicTacToe_2D() {
        super(16);
    }

    public TicTacToe_2D(char[] array){
        super(array);
    }

    public TicTacToe_2D(char[] array, ArrayList<Integer> emptyCell){
        super(array,emptyCell);
    }

    public TicTacToe_2D(TicTacToe_2D another) {
        super(another);
    }

    /**
     * methode findSolutionFromCell avec profondeur predefini pour le tictactoe 2D
     * @see #findSolutionFromCell(int, int, int)
     */
    public boolean findSolutionFromCell(int line, int column) {
        return super.findSolutionFromCell(line, column, 1);
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
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if (j != 0) {
                    s.append("|");
                }
                s.append(cells[4 * i + j]);
            }
            s.append(Utilities.LINE_SEPARATOR);
        }
        return s.toString();
    }

    /**
     * methode setCell avec profondeur predefini pour le tictactoe 2D
     * @see #setCell(char, int, int ,int)
     */
    public void setCell(char value, int line, int column){
        this.setCell(value, line, column,1);
    }

    public char getCell(int line, int column) {
        return super.getCell(line, column, 1);
    }

    @Override
    public int heuristicEval() {
        char x = 'X';
        char o = 'O';
        int eval = 0;
        int nbO = 0;
        int nbX =0;

        // parcours de la ligne
        for(int i = 0;i<4;i++) {
            for (int j = 4 * i; j < i + 4; j++) {
                if (this.cells[j] == x) {
                    nbX++;
                }else if (this.cells[j] == o) {
                    nbO++;
                }
            }
            if (nbX > 0 && nbO == 0 ){
                eval += heuristic[nbX];
            }
            else if (nbO > 0 && nbX == 0 ){
                eval -= heuristic[nbO];
            }
            nbX = 0;
            nbO = 0;
        }
        for(int i = 0;i<4;i++) {
            for (int j = i; j < 16; j=j+4) {
                if (this.cells[j] == x) {
                    nbX++;
                }else if (this.cells[j] == o) {
                    nbO++;
                }
            }
            if (nbX > 0 && nbO == 0 ){
                eval += heuristic[nbX];
            }
            else if (nbO > 0 && nbX == 0 ){
                eval -= heuristic[nbO];
            }
            nbX = 0;
            nbO = 0;
        }
        for(int i =0;i<16;i=i+5){
            if (this.cells[i] == x) {
                nbX++;
            }else if (this.cells[i] == o) {
                nbO++;
            }


        }
        if (nbX > 0 && nbO == 0 ){
            eval += heuristic[nbX];
        }
        else if (nbO > 0 && nbX == 0 ){
            eval -= heuristic[nbO];
        }
        nbX = 0;
        nbO = 0;
        for(int i =3;i<13;i=i+3){
            if (this.cells[i] == x) {
                nbX++;
            }else if (this.cells[i] == o) {
                nbO++;
            }
        }
        if (nbX > 0 && nbO == 0 ){
            eval += heuristic[nbX];
        }
        else if (nbO > 0 && nbX == 0 ){
            eval -= heuristic[nbO];
        }
        return eval;
    }

}
