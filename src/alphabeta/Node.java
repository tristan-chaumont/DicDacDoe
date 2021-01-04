package alphabeta;

import tictactoe.StructureTicTacToe;

import java.util.ArrayList;

public class Node extends TreeNode{

    // Enfants du Nœud courant
    private ArrayList<TreeNode> children;
    // type du niveau du nœud ( soit min pour 'O' soit max pour 'X')
    private String type;
    // alpha et beta ne peuvent prendre que les valeurs 0 et 1 (-1 pour victoire des cercles, 0 pour égalité, 1 pour la victoire des croix)
    private Integer alpha = Integer.MIN_VALUE,beta = Integer.MAX_VALUE;

    public Node(StructureTicTacToe s, String t, Integer a, Integer b){
        super(s);
        children = new ArrayList<TreeNode>();
        type = t;
        // condition pour empêcher de mettre à null ou à une valeur invalide la valeur d'alpha
        if(a == -1 || a == 0 || a == 1)
            alpha = a;
        // condition pour empêcher de mettre à null ou à une valeur invalide la valeur de beta
        if(b == -1 || b == 0 || b == 1)
            beta = b;
    }

    public ArrayList<TreeNode> alphaBeta(){
        ArrayList<TreeNode> path = new ArrayList<TreeNode>();
        ArrayList<TreeNode> pathTemp = new ArrayList<TreeNode>();
        Integer val;
        if(type.equals("max")){
            for (TreeNode child: children) {
                if(child instanceof Leaf) {
                    val = child.getValue();
                }else {
                    pathTemp = ((Node) child).alphaBeta();
                    val = ((Node) child).getValue();
                }
                if (val > alpha)
                    alpha = val;
                    if(path.size() > 0)
                        path.remove(0);
                    path.add(child);
                    if (child instanceof Node)
                        path.addAll(pathTemp);
                if (beta <= alpha)
                    break;
            }
        }else {
            for (TreeNode child: children) {
                if(child instanceof Leaf) {
                    val = child.getValue();
                }else {
                    pathTemp = ((Node) child).alphaBeta();
                    val = ((Node) child).getValue();
                }
                if (val > beta) {
                    beta = val;
                    if(path.size() > 0)
                        path.remove(0);
                    path.add(child);
                    if (child instanceof Node)
                        path.addAll(pathTemp);
                }
                if (beta <= alpha)
                    break;
            }
        }
        setValue(beta);
        return path;
    }

    public void addChildren(TreeNode n){
        children.add(n);
    }

    public void setAlpha(int a){
        alpha = a;
    }

    public void setBeta(int b){
        beta = b;
    }

    public int getAlpha(){ return alpha;}

    public int getBeta(){ return beta;}
    
    public String getType(){
        return type;
    }

    public ArrayList<TreeNode> getChildren(){
        return children;
    }
}
