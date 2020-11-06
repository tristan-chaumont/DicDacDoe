package TicTacToe;

public class TicTacToe_2D extends StructureTicTacToe {

    public TicTacToe_2D() {
        super(16);
    }

    public TicTacToe_2D(char [] array){
        super(array);
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
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean solutionColumn(int cell, char state) {
        //haut de la colonne
        int begin = cell%4;
        //parcours de la colonne
        for(int i = begin;i<16;i=i+4){
            if (this.cells[i] != state){
                return false;
            }
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
                    return false;
                }

            }
            return true;
        }
        //verifie si il fait partie de la diagonale haut droite a bas gauche
        else if(cell%3 == 0){
            //parcours de cette diagonale
            for(int i =3;i<13;i=i+3){
                if (this.cells[i] != state){
                    return false;
                }

            }
            return true;
        }
        //si il ne fait pas partie d'une de ces 2 diagonales retoune zero
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

    /**
     * methode setCell avec profondeur predefini pour le tictactoe 2D
     * @see #setCell(char, int, int ,int)
     */
    public void setCell(char value, int line, int column){
        this.setCell(value,line,column,1);
    }
}
