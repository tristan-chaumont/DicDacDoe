package TicTacToe;

public class TicTacToe_2D extends StructureTicTacToe {

    public TicTacToe_2D() {
        super(16);
    }

    public TicTacToe_2D(char [] array){
        super(array);
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

    @Override
    public String toString() {
        StringBuilder s= new StringBuilder();
        s.append("---------\n");
        for(int i=0;i<4;i++){
            s.append("|");
            for(int j =0;j<4;j++){
                s.append(cells[4 * i + j]).append("|");
            }
            s.append("\n");
            s.append("---------\n");
        }
        return s.toString();
    }
}
