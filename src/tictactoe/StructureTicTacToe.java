package tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class StructureTicTacToe {

    protected static int[]  heuristic = {0,1,3,7,9999};
    protected char [] cells;
    protected ArrayList<Integer> emptyCell;
    protected ArrayList<Integer> winningCells;

    StructureTicTacToe(StructureTicTacToe another){
        this.cells = Arrays.copyOf(another.cells, another.cells.length);
        this.emptyCell = new ArrayList<>(another.getEmptyCell());
        this.winningCells = new ArrayList<>(another.getWinningCells());
    }

    StructureTicTacToe(int size){
        cells = new char[size];
        emptyCell = new ArrayList<>();
        Arrays.fill(cells,' ');
        emptyCell = IntStream.range(0, 16).boxed().collect(Collectors.toCollection(ArrayList::new));
        winningCells = new ArrayList<>();
    }

    StructureTicTacToe(char [] array){
        cells = array;
        emptyCell = new ArrayList<>();
        winningCells = new ArrayList<>();
        for(int i = 0;i<cells.length;i++){
            if(cells[i] == ' '){
                emptyCell.add(i);
            }
        }
    }

    StructureTicTacToe(char [] array,ArrayList<Integer> emptyCell){
        cells = array;
        this.emptyCell = new ArrayList<>(emptyCell);
        winningCells = new ArrayList<>();
    }


    /**
     * Recherche si solution est gagnante depuis une case
     * @param cell emplacement de la case
     * @return true si on a une situation gagnante et false si aucune situation n'est gagnante
     */
    public boolean findSolutionFromCell(int cell){
        char state = this.cells[cell];
        return (solutionLine(cell,state) || solutionColumn(cell,state) || solutionDepth(cell,state) ||solutionDiagonal(cell,state));
    }

    /**
     *Recherche la solution a l'aide des coordonnées d'une case
     * @param line numero de la ligne compris entre 1 et 4
     * @param column numero de la colonne compris entre 1 et 4
     * @param depth profondeur du  entre 1 et 4
     * @see #findSolutionFromCell(int)
     */
    protected boolean findSolutionFromCell(int line, int column, int depth) {
        int cell = (depth - 1) * 16 + (line - 1) * 4 + column - 1;
        return this.findSolutionFromCell(cell);
    }

    /**
     * Recherche une solution en ligne
     * @param cell
     * @param state X ou O
     * @return
     */
    public boolean solutionLine(int cell,char state){
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

    /**
     * Recherche une solution en colonne
     * @param cell
     * @param state X ou O
     * @return
     */
    public abstract boolean solutionColumn(int cell,char state);

    /**
     * Recherche une solution en diagonale
     * @param cell
     * @param state X ou O
     * @return
     */
    public abstract boolean solutionDiagonal(int cell,char state);

    public abstract boolean solutionDepth(int cell,char state);

    /**
     * pour changer l'état d'une case
     * @param value nouvel état
     * @param line numero de la ligne compris entre 1 et 4
     * @param column numero de la colonne compris entre 1 et 4
     * @param depth profondeur du  entre 1 et 4
     */
    public void setCell(char value, int line, int column, int depth){
        int cell = (depth-1)*16 + (line-1)*4 + column-1;
        if(this.cells[cell] == ' ') {
            this.cells[cell] = value;
            emptyCell.remove(Integer.valueOf(cell));
        }
    }

    public void setCell(char value, int cell){
        if(this.cells[cell] == ' ') {
            this.cells[cell] = value;
            emptyCell.remove((Integer) cell);
        }
    }

    protected char getCell(int line,int column, int depth){
        int cell = (depth-1)*16 + (line-1)*4 + column-1;
        if(cell < cells.length) {
            return this.cells[cell];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public ArrayList<Integer> getEmptyCell() {
        return emptyCell;
    }

    public void clearWinningCells() {
        winningCells.clear();
    }

    public void setWinningCell(Integer cell) {
        this.winningCells.add(cell);
    }

    public ArrayList<Integer> getWinningCells() {
        return winningCells;
    }

    public void setCells(char[] cells) {
        this.cells = cells.clone();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StructureTicTacToe that = (StructureTicTacToe) o;
        return Arrays.equals(cells, that.cells);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(cells);
    }

    public char[] getCells() {
        return cells;
    }

    public abstract int heuristicEval();
}

