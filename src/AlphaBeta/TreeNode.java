package AlphaBeta;

import TicTacToe.StructureTicTacToe;

public class TreeNode {

    private Integer value;
    private StructureTicTacToe situation;

    public TreeNode(StructureTicTacToe s){
        situation = s;
        value = null;
    }

    public TreeNode(StructureTicTacToe s, Integer v){
        situation = s;
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
}
