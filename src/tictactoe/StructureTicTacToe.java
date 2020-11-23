package tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class StructureTicTacToe {

    protected char [] cells;
    protected ArrayList<Integer> emptyCell;

    StructureTicTacToe(StructureTicTacToe another){
        this.cells = Arrays.copyOf(another.cells, another.cells.length);
        this.emptyCell = new ArrayList<>();
        Collections.copy(another.emptyCell, this.emptyCell);
    }

    StructureTicTacToe(int size){
        cells = new char[size];
        emptyCell = new ArrayList<>();
        Arrays.fill(cells,' ');
        emptyCell = IntStream.range(0, 16).boxed().collect(Collectors.toCollection(ArrayList::new));
    }

    StructureTicTacToe(char [] array){
        cells = array;
        emptyCell = new ArrayList<>();
        for(int i = 0;i<cells.length;i++){
            if(cells[i] == ' '){
                emptyCell.add(i);
            }
        }
    }

    StructureTicTacToe(char [] array,ArrayList<Integer> emptyCell){
        cells = array;
        this.emptyCell = new ArrayList<>(emptyCell);
    }


    /**
     * Recherche si solution est gagnante depuis une case
     * @param cell emplacement de la case
     * @return true si on a une situation gagnante et false si aucune situation n'est gagnante
     */
    public boolean findSolutionFromCell(int cell){
        char state = this.cells[cell];
        return (solutionLine(cell,state) || solutionColumn(cell,state) || solutionDiagonal(cell,state));
    }

    /**
     *Recherche la solution a l'aide des coordonnées d'une case
     * @param line numero de la ligne compris entre 1 et 4
     * @param column numero de la colonne compris entre 1 et 4
     * @param depth profondeur du  entre 1 et 4
     * @see #findSolutionFromCell(int)
     */
    protected boolean findSolutionFromCell(int line,int column, int depth) {
        int cell = (depth - 1) * 16 + (line - 1) * 4 + column - 1;
        return this.findSolutionFromCell(cell);
    }

    /**
     * Recherche une solution en ligne
     * @param cell
     * @param state X ou O
     * @return
     */
    public abstract boolean solutionLine(int cell,char state);

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

    /**
     * pour changer l'état d'une case
     * @param value nouvel état
     * @param line numero de la ligne compris entre 1 et 4
     * @param column numero de la colonne compris entre 1 et 4
     * @param depth profondeur du  entre 1 et 4
     */
    protected void setCell(char value, int line, int column, int depth){
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

    public void setCells(char[] cells) {
        this.cells = cells.clone();
    }
}

