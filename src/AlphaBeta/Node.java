package AlphaBeta;

import java.util.ArrayList;

public class Node extends TreeNode{

    // Enfants du Nœud courant
    private ArrayList<Node> children;
    // type du niveau du nœud ( soit min soit max )
    private String type;
    // alpha et beta ne peuvent prendre que les valeurs 0 et 1 ( 0 pour la victoire des cercles, 1 pour la victoire des croix)
    private Integer alpha = Integer.MIN_VALUE,beta = Integer.MAX_VALUE;

    public Node(String t, Integer a, Integer b){
        super();
        children = new ArrayList<Node>();
        type = t;
        // condition pour empêcher de mettre à null la valeur d'alpha
        if(a == 0 || a == 1)
            alpha = a;
        // condition pour empêcher de mettre à null la valeur de beta
        if(b == 0 || b == 1)
            beta = b;
    }

    public Integer bestValue(){
        Integer val;
        if(type.equals("max")){
            for (TreeNode child: children) {
                if(child instanceof Leaf)
                    val = child.getValue();
                else
                    val = ((Node)child).bestValue();
                if (val > alpha)
                    alpha = val;
                if (beta <= alpha)
                    break;
            }
        }else {
            for (TreeNode child: children) {
                if(child instanceof Leaf)
                    val = child.getValue();
                else
                    val = ((Node)child).bestValue();
                if (val > beta)
                    beta = val;
                if (beta <= alpha)
                    break;
            }
        }
        setValue(beta);
        return getValue();
    }

    public void addChildren(Node n){
        children.add(n);
    }

    public void setAlpha(int a){
        alpha = a;
    }

    public void setBeta(int b){
        beta = b;
    }
}
