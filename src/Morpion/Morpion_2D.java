package Morpion;

public class Morpion_2D extends StructureMorpion{

    public Morpion_2D() {
        super(16);
    }


    @Override
    public boolean solutionLine(int cell, char state) {
        int begin = cell - cell%4;
        for(int i = begin;i<begin+4;i++){
            if (this.cells[i] != state){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean solutionColumn(int cell, char state) {
        int begin = cell%4;
        for(int i = begin;i<16;i=i+4){
            if (this.cells[i] != state){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean solutionDiagonal(int cell, char state) {
        if(cell%5 == 0){
            for(int i =0;i<16;i=i+5){
                if (this.cells[i] != state){
                    return false;
                }

            }
            return true;
        }
        else if(cell%3 == 0){
            for(int i =3;i<13;i=i+3){
                if (this.cells[i] != state){
                    return false;
                }

            }
            return true;
        }
        return false;

    }
}
