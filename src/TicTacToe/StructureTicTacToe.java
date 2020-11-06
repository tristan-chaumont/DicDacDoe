package TicTacToe;

import java.util.Arrays;

public abstract class StructureTicTacToe {

    char [] cells;


    StructureTicTacToe(int size){
        cells = new char[size];
        Arrays.fill(cells,' ');
    }

    StructureTicTacToe(char [] array){
        cells = array;
    }

    public boolean findSolutionFromCell(int cell){
        char state = this.cells[cell];
        return (solutionLine(cell,state) || solutionColumn(cell,state) || solutionDiagonal(cell,state));
    };

    public abstract boolean solutionLine(int cell,char state);

    public abstract boolean solutionColumn(int cell,char state);

    public abstract boolean solutionDiagonal(int cell,char state);



}

