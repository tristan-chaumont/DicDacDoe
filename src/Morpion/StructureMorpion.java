package Morpion;

public abstract class Structure_Morpion {

    int [] cells;


    Structure_Morpion(int size){
        cells = new int[size];
    }

    public abstract boolean find_solution_from_cell(int cell);

}
