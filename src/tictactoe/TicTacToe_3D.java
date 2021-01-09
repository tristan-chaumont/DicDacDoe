package tictactoe;

import utilities.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TicTacToe_3D extends StructureTicTacToe {

    private static final Integer[][] diagonalState = {{0,5,10,15}, // diag cross column/line
                                                {12,9,6,3},
                                                {16,21,26,31},
                                                {28,25,22,19},
                                                {32,37,42,47},
                                                {44,41,38,35},
                                                {48,53,58,63},
                                                {60,57,54,51},
                                                {0,17,34,51}, // diag cross depth/line
                                                {48,33,18,3},
                                                {4,21,38,55},
                                                {52,37,22,7},
                                                {8,25,42,59},
                                                {56,41,26,11},
                                                {12,29,46,63},
                                                {60,45,30,15},
                                                {0,20,40,60}, // diag depth/column
                                                {48,36,24,12},
                                                {1,21,41,61},
                                                {49,37,25,13},
                                                {2,22,42,62},
                                                {50,38,26,14},
                                                {3,23,43,63},
                                                {51,39,27,15},
                                                {0,21,42,63}, //cube vertex diag
                                                {48,37,26,15},
                                                {12,25,38,51},
                                                {60,41,22,3}
                                                };

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
    public boolean findSolutionFromCell(int line, int column, int depth) {
        return super.findSolutionFromCell(line, column, depth);
    }

    @Override
    public boolean solutionColumn(int cell, char state) {
        //haut de la colonne

        int begin = ((int)Math.floor(cell/16)) * 16 + cell%4;

        //parcours de la colonne
        for(int i = begin;i<=begin+12;i=i+4){
            if (this.cells[i] != state) {
                clearWinningCells();
                return false;
            }
            setWinningCell(i);
        }
        return true;
    }

    @Override
    public boolean solutionDepth(int cell, char state) {
        int begin = cell%16;
        for(int i = begin;i<=63;i=i+16){
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
        for(int i =0;i < diagonalState.length;i++){
            for (int j=0;j<4;j++){
                if(cell == diagonalState[i][j]){
                    if (testDiagonal(i, state)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean testDiagonal(int line,char state){
        for(int i=0;i<4;i++){
            if (this.cells[diagonalState[line][i]] != state) {
                clearWinningCells();
                return false;
            }
            setWinningCell(diagonalState[line][i]);
        }
        return true;
    }

    @Override
    public char getCell(int line,int column, int depth){
        return super.getCell(line, column, depth);
    }

    @Override
    public int heuristicEval() {
        char x = 'X';
        char o = 'O';
        int eval = 0;
        int nbO = 0;
        int nbX =0;

        // parcours de la ligne
        for(int i = 0;i<16;i++) {
            for (int j = 4 * i; j < i*4 + 4; j++) {
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
        int begin;
        for(int i = 0;i<16;i++) {
            begin = i%4+16*(i/4);
            for (int j = begin; j < begin+12; j=j+4) {
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
        for(int i =0;i < diagonalState.length;i++){
            for (int j=0;j<4;j++){
                if (this.cells[diagonalState[i][j]] == x) {
                    nbX++;
                }else if (this.cells[diagonalState[i][j]] == o) {
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
        return  eval;
    }

    public static ArrayList<char []> rotate(char [] cells){
        ArrayList<char []> rotate = new ArrayList<>();
        char [] opposite_diag_vertex = rotate_opposite_diag_vertex(cells);
        char [] opposite_diag = rotate_opposite_diag(cells);
        char [] opposite_diag_opposite_vertex = rotate_opposite_diag_vertex(opposite_diag);
        rotate.add(cells);
        rotate.add(opposite_diag_vertex);
        rotate.add(opposite_diag);
        rotate.add(opposite_diag_opposite_vertex);
        return rotate;


    }

    public static char [] rotate_opposite_diag_vertex(char [] cells){
        char [] cellsBis = new char[64];
        for(int i = 0; i < cells.length;i++){
            cellsBis[63-i] = cells[i];
        }
        return cellsBis;

    }

    public static char [] rotate_opposite_diag(char [] cells){
        char [] cellsBis = new char[64];
        for(int i = 0; i < 16;i++){
            cellsBis[15-i] = cells[i];
            cellsBis[31-i] = cells[i+16];
            cellsBis[47-i] = cells[i+32];
            cellsBis[63-i] = cells[i+48];
        }
        return cellsBis;

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
