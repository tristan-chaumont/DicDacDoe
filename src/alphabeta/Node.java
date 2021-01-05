package alphabeta;

import tictactoe.StructureTicTacToe;

import java.util.ArrayList;

public class Node extends TreeNode{

    // Enfants du Nœud courant
    private ArrayList<TreeNode> children;
    // type du niveau du nœud ( soit min pour 'O' soit max pour 'X')
    private String type;
    // alpha et beta ne peuvent prendre que les valeurs 0 et 1 (-1 pour victoire des cercles, 0 pour égalité, 1 pour la victoire des croix)


    public Node(StructureTicTacToe s, String t, Integer a, Integer b){
        super(s);
        children = new ArrayList<TreeNode>();
        type = t;
        // condition pour empêcher de mettre à null ou à une valeur invalide la valeur d'alpha
    }

    public Node(StructureTicTacToe s, String t){
        super(s);
        children = new ArrayList<TreeNode>();
        type = t;
    }

    public void addChildren(TreeNode n){
        children.add(n);
    }



    public String getType(){
        return type;
    }

    public ArrayList<TreeNode> getChildren(){
        return children;
    }
}
