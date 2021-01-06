package alphabeta;

import tictactoe.StructureTicTacToe;
import tictactoe.TicTacToe_2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Tree {

    private Node root;
    private int dimension;
    private char player;
    private  int maxDepth;
    //private int freeCells;
    private HashMap<StructureTicTacToe,TreeNode> duplicate;
    static int total = 0;
    private static ArrayList<TreeNode> path = new ArrayList<>();
    private static ArrayList<TreeNode> actualPath = new ArrayList<>();

    public Tree(int dim, char p){
        dimension = dim;
        duplicate = new HashMap<>();
        player = p;
        maxDepth = 7;
        if(player == 'X');
        root = new Node(new TicTacToe_2D(), "max");
        /*if(dimension == 2)
            fillTree(root,16);
        else
            fillTree(root,64);*/
        alphabeta2D(root,Integer.MIN_VALUE, Integer.MAX_VALUE,1);
    }

    public Tree(int dim, char p,StructureTicTacToe sttt){
        dimension = dim;
        duplicate = new HashMap<>();
        player = p;
        maxDepth = 7;
        if(player == 'X') {
            root = new Node(new TicTacToe_2D((TicTacToe_2D) sttt), "max");
        }
        else{
        root = new Node(new TicTacToe_2D((TicTacToe_2D) sttt), "min");}
        /*if(dimension == 2)
            fillTree(root,16);
        else
            fillTree(root,64);*/
        alphabeta2D(root,Integer.MIN_VALUE, Integer.MAX_VALUE,1);
    }

    public int alphabeta2D(TreeNode cn,int alpha,int beta,int depth){
        int v;
        //On test si le fils est une feuille
        if(cn instanceof Leaf){
            //path.add(cn);
            return cn.getValue();
        }else {
            //On veut savoir le nombre de case vide
            ArrayList<Integer> emptyCells = cn.getSituation().getEmptyCell();
            int size = emptyCells.size();



            //On veut connaître le joueur actuel
                //Si joueur est min ( cercle )
            if(((Node)cn).getType().equals("min")) {
                //On veut créer tous les fils sauf si condition alpha >= beta validée
                v = Integer.MAX_VALUE;
                if(size == 1) {

                }
                for (int i = 0; i < size; i++) {
                    //Création de la nouvelle situation
                    int pos = emptyCells.get(i);
                    TicTacToe_2D newSituation = new TicTacToe_2D((TicTacToe_2D)cn.getSituation());
                    newSituation.setCell('O',pos);

                    //On regarde si elle est solution
                    //Si c'est la somution on crée une feuille
                    TreeNode t;
                    if(newSituation.findSolutionFromCell(pos) || depth == maxDepth){
                        int val = newSituation.heuristicEval();
                        //System.out.println(val);
                        t = new Leaf(newSituation,val);
                    }
                    //Sinon on applique la récursivité
                    else if(size != 1){
                        t = new Node(newSituation, "max");

                    }
                    else{
                        t = new Leaf(newSituation, 0);
                    }
                    t.setPos(pos);
                    ((Node) cn).addChildren(t);
                    //On check la condition alpha beta
                    v = Math.min(v,alphabeta2D(t,alpha,beta,depth+1));
                    //Si elle est validé on retounre la valeur
                    if(alpha >= v){
                        cn.setValue(v);
                        return v;
                    }
                    //Sinon on met v dans beta
                    beta = Math.min(beta,v);
                }
            }
                //sinon ( le jouer est max (croix))
            else{
                // On veut créer tous les fils sauf si condition alpha >= beta validée
                v = Integer.MIN_VALUE;
                for (int i = 0; i < size; i++){
                    //Création de la nouvelle situation
                    int pos = emptyCells.get(i);
                    TicTacToe_2D newSituation = new TicTacToe_2D((TicTacToe_2D)cn.getSituation());
                    newSituation.setCell('X',pos);
                    //On regarde si elle est solution
                    //Si c'est la solution on crée une feuille
                    TreeNode t;
                    if(newSituation.findSolutionFromCell(pos) || depth == maxDepth){
                        int val = newSituation.heuristicEval();
                        //System.out.println(val);
                        t = new Leaf(newSituation,val);
                    }
                    //Sinon on applique la récursivité
                    else if(size != 1){
                        t = new Node(newSituation, "min");
                    }
                    else{
                        t = new Leaf(newSituation, 0);
                    }
                    t.setPos(pos);
                    ((Node) cn).addChildren(t);
                    //On check la condition alpha beta
                    v = Math.max(v,alphabeta2D(t,alpha,beta,depth+1));
                    //Si elle est validé on retounre la valeur
                    if(v >= beta){
                        cn.setValue(v);
                        return v;
                    }
                    //Sinon on met v dans alpha
                    alpha = Math.max(alpha,v);
                }
            }
            cn.setValue(v);
            return v;
        }
    }
/*
    public void fillTree(Node cn, int freecell){
        if(dimension == 2){
            if(root == null){
                if(player == 'X')
                    cn = new Node(new TicTacToe_2D(), "max", Integer.MIN_VALUE, Integer.MAX_VALUE);
                else
                    cn = new Node(new TicTacToe_2D(), "min", Integer.MIN_VALUE, Integer.MAX_VALUE);
            }
            TicTacToe_2D situation = (TicTacToe_2D) cn.getSituation();
            ArrayList<Integer> emptyCells = new ArrayList<>(situation.getEmptyCell());
            TicTacToe_2D newSituation;
            String newType;
            char newPlayer;
            if(cn.getType().equals("max")) {
                newType = "min";
                newPlayer = 'X';
            }else {
                newType = "max";
                newPlayer = 'O';
            }
            if(freecell == 1){
                newSituation = new TicTacToe_2D(situation);
                int pos = emptyCells.get(0);
                newSituation.setCell(newPlayer, pos);
                if (!(this.duplicate.containsKey(newSituation))) {
                    TreeNode t;
                    if (newSituation.findSolutionFromCell(pos)) {
                        if (newPlayer == 'X')
                            t = new Leaf(newSituation, 1);
                        else
                            t = new Leaf(newSituation, -1);
                    } else {
                        t = new Leaf(newSituation, 0);
                    }
                    cn.addChildren(t);
                    if(cn.getType().equals("max")){
                        cn.setAlpha(t.getValue());
                        cn.setValue(t.getValue());
                    }else{
                        cn.setBeta(t.getValue());
                        cn.setValue(t.getValue());
                    }
                    this.duplicate.put(newSituation,t);
                }else{
                    TreeNode  t = duplicate.get(newSituation);
                    t.setDuplicate(true);
                    cn.addChildren(t);
                }
            }else {
                int tmpfreecell = freecell;
                // Pour chaque case libre on crée une situation
                for (int i = 0; i < tmpfreecell; i++) {
                    newSituation = new TicTacToe_2D(situation);
                    int pos = emptyCells.get(i);
                    newSituation.setCell(newPlayer, pos);
                    if (!(this.duplicate.containsKey(newSituation))) {
                        TreeNode t;
                        if (newSituation.findSolutionFromCell(pos)) {

                            if (newPlayer == 'X') {
                                t = new Leaf(newSituation, 1);
                            }
                            else {
                                t = new Leaf(newSituation, -1);
                            }
                            if (newPlayer == player)
                                break;
                        }
                        else {
                            if(cn.getType().equals("max"))
                                t = new Node(newSituation, newType, cn.getAlpha(), Integer.MAX_VALUE);
                            else
                                t = new Node(newSituation, newType, Integer.MIN_VALUE, cn.getBeta());
                            this.duplicate.put(newSituation,t);
                            fillTree((Node)t,tmpfreecell - 1);
                        }
                        cn.addChildren(t);
                        if(t instanceof Leaf){
                            try{
                                int valChild = t.getValue();

                                if(cn.getType().equals("max")) {
                                    if(valChild > cn.getAlpha()){
                                        cn.setAlpha(valChild);
                                        cn.setValue(valChild);
                                    }
                                }else{
                                    if(valChild < cn.getBeta()){
                                        cn.setBeta(valChild);
                                        cn.setValue(valChild);
                                    }
                                }

                                if(cn.getAlpha() >= cn.getBeta()){
                                    tmpfreecell = 0;
                                }
                            }catch (Exception e){
                                e.getStackTrace();
                            }
                        }
                        if(t instanceof Node){
                            try{
                                int valChild = t.getValue();


                                if(cn.getType().equals("max")) {
                                    if(valChild > cn.getAlpha()){
                                        cn.setAlpha(valChild);
                                        cn.setValue(valChild);
                                    }
                                }else{
                                    if(valChild < cn.getBeta()){
                                        cn.setBeta(valChild);
                                        cn.setValue(valChild);
                                    }
                                }

                                if(cn.getAlpha() >= cn.getBeta()){
                                    ;
                                }
                            }catch (Exception e){
                                e.getStackTrace();
                            }
                        }
                    }
                    else{
                        TreeNode t = duplicate.get(newSituation);
                        t.setDuplicate(true);
                        cn.addChildren(t);
                    }
                }
            }
            total += cn.getChildren().size();
            System.out.println(total);
            //tmpfreecell--;
        }
    }*/

    public int nextStep(){
        int val = root.getValue();
        for(TreeNode t : root.getChildren()){
            if (t.getValue() == val){
                return t.getPos();
            }
        }
        return -1;
    }



    public Node getRoot() {
        return root;
    }
}
