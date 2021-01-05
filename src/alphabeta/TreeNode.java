package alphabeta;

import tictactoe.StructureTicTacToe;

import java.util.Arrays;
import java.util.Objects;

public class TreeNode {

    private Integer value;
    private StructureTicTacToe situation;
    private boolean duplicate;
    private int pos;



    public TreeNode(StructureTicTacToe s){
        situation = s;
        duplicate = false;
        value = null;
    }

    public TreeNode(StructureTicTacToe s, Integer v){
        situation = s;
        duplicate = false;
        value = v;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer v) {
        value = v;
    }

    public StructureTicTacToe getSituation(){
        return situation;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(situation.getCells());
    }


    public boolean isDuplicate() {
        return duplicate;
    }

    public void setDuplicate(boolean duplicate) {
        this.duplicate = duplicate;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }
}
