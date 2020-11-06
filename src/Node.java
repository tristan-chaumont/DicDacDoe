import java.util.ArrayList;

public class Node {

    private ArrayList<Node> children;
    private String type;
    private int alpha = Integer.MIN_VALUE,beta = Integer.MAX_VALUE;

    public Node(String t, int a, int b){
        children = new ArrayList<Node>();
        type = t;
        if(a == 0 || a == 1)
            alpha = a;
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
