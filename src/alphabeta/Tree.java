package alphabeta;

import tictactoe.StructureTicTacToe;
import tictactoe.TicTacToe_2D;
import tictactoe.TicTacToe_3D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class Tree {

    private Node root;
    private int dimension;
    private char player;
    private  int maxDepth;
    //private int freeCells;

    public Tree(int dim, char p){
        dimension = dim;
        player = p;
        long nbCase = (long)Math.pow(4,dim);
        int i = 0;
        long produit = nbCase;
        while(produit < 10000000000L){
            produit *= --nbCase;
            //System.out.println(produit);
            i++;
        }
        if(i%2 == 0){
            i--;
        }
        maxDepth = i;
        String typePlayer;
        if(player == 'X') {
            typePlayer = "max";
        }
        else {
            typePlayer = "min";
        }

        if(dimension == 2) {
            root = new Node(new TicTacToe_2D(), typePlayer);
            alphabeta2D(root, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        }else{
            root = new Node(new TicTacToe_3D(), typePlayer);
            alphabeta3D(root,Integer.MIN_VALUE, Integer.MAX_VALUE,1);
        }
    }

    public Tree(int dim, char p,StructureTicTacToe sttt){
        dimension = dim;
        player = p;
        long truc = sttt.getEmptyCell().size();
        int i = 0;
        long produit = truc;
        while(produit < 10000000000L && truc > 1){
            produit *= --truc;
            //System.out.println(produit);
            i++;
        }
        if(i%2 == 0){
            i--;
        }
        maxDepth = i;
        String typePlayer;
        if(player == 'X') {
            typePlayer = "max";
        }
        else {
           typePlayer = "min";
        }

        if (dimension == 2) {
            root = new Node(new TicTacToe_2D((TicTacToe_2D) sttt), typePlayer);
            alphabeta2D(root, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        } else {
            root = new Node(new TicTacToe_3D((TicTacToe_3D) sttt), typePlayer);
            alphabeta3D(root,Integer.MIN_VALUE, Integer.MAX_VALUE,1);
        }
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
            Collections.shuffle(emptyCells);
            int size = emptyCells.size();



            //On veut connaître le joueur actuel
                //Si joueur est min ( cercle )
            if(((Node)cn).getType().equals("min")) {
                //On veut créer tous les fils sauf si condition alpha >= beta validée
                v = Integer.MAX_VALUE;
                for (int i = 0; i < size; i++) {
                    //Création de la nouvelle situation
                    int pos = emptyCells.get(i);
                    TicTacToe_2D newSituation = new TicTacToe_2D((TicTacToe_2D)cn.getSituation());
                    newSituation.setCell('O',pos);

                    //On regarde si elle est solution
                    //Si c'est la somution on crée une feuille
                    TreeNode t;
                    if((newSituation.findSolutionFromCell(pos) || depth == maxDepth )){
                        int val = newSituation.heuristicEval();
                        //System.out.println(val);
                        t = new Leaf(newSituation,val);
                    }
                    //Sinon on crée un nœud
                    else{
                        t = new Node(newSituation, "max");

                    }
                    // On applique la récursivité
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
                    else{
                        t = new Node(newSituation, "min");
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

    public int alphabeta3D(TreeNode cn,int alpha,int beta,int depth){
        int v;
        //On test si le fils est une feuille
        if(cn instanceof Leaf){
            return cn.getValue();
        }else {
            //On veut savoir le nombre de case vide
            ArrayList<Integer> emptyCells = cn.getSituation().getEmptyCell();
            Collections.shuffle(emptyCells);
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
                    TicTacToe_3D newSituation = new TicTacToe_3D((TicTacToe_3D)cn.getSituation());
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
                    if (depth == 1) {
                        t.setPos(pos);
                        ((Node) cn).addChildren(t);
                    }
                    //On check la condition alpha beta
                    v = Math.min(v,alphabeta3D(t,alpha,beta,depth+1));
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
                    TicTacToe_3D newSituation = new TicTacToe_3D((TicTacToe_3D)cn.getSituation());
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
                    if (depth == 1) {
                        t.setPos(pos);
                        ((Node) cn).addChildren(t);
                    }
                    //On check la condition alpha beta
                    v = Math.max(v,alphabeta3D(t,alpha,beta,depth+1));
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
