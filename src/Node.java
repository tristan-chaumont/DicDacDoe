import java.util.ArrayList;

public class Node {

    // Enfants du Nœud courant
    private ArrayList<Node> children;
    // type du niveau du nœud ( soit min soit max )
    private String type;
    // alpha et beta ne peuvent prendre que les valeurs 0 et 1 ( 0 pour la victoire des cercles, 1 pour la victoire des croix)
    private int alpha = Integer.MIN_VALUE,beta = Integer.MAX_VALUE;

    public Node(String t, int a, int b){
        children = new ArrayList<Node>();
        type = t;
        // condition pour empêcher de mettre à null la valeur d'alpha
        if(a == 0 || a == 1)
            alpha = a;
        // condition pour empêcher de mettre à null la valeur de beta
        if(b == 0 || b == 1)
            beta = b;
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
