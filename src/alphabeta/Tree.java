package alphabeta;

import tictactoe.TicTacToe_2D;

import java.util.ArrayList;

public class Tree {

    private Node root;
    private int dimension;
    private char player;
    private int freeCells = 16;

    public Tree(int dim, char p){
        dimension = dim;
        player = p;
        if(player == 'X')
        root = new Node(new TicTacToe_2D(), "max", Integer.MIN_VALUE, Integer.MAX_VALUE);
        fillTree(root);
    }

    public TreeNode fillTree(Node cn){
        Node currentNode = cn;
        if(dimension == 2){
            if(root == null){
                if(player == 'X')
                    currentNode = new Node(new TicTacToe_2D(), "max", Integer.MIN_VALUE, Integer.MAX_VALUE);
                else
                    currentNode = new Node(new TicTacToe_2D(), "min", Integer.MIN_VALUE, Integer.MAX_VALUE);
            }
            TicTacToe_2D situation = (TicTacToe_2D) currentNode.getSituation();
            ArrayList<Integer> emptyCells = situation.getEmptyCell();
            TicTacToe_2D newSituation;
            String newType;
            char newPlayer;
            if(currentNode.getType().equals("max")) {
                newType = "min";
                newPlayer = 'X';
            }else {
                newType = "max";
                newPlayer = 'O';
            }
            if(freeCells == 1){
                newSituation = new TicTacToe_2D(situation);
                newSituation.setCell(newPlayer,0);
                if(newSituation.findSolutionFromCell(emptyCells.get(0))) {
                    if(newPlayer == 'X')
                        currentNode.addChildren(new Leaf(newSituation,1));
                    else
                        currentNode.addChildren(new Leaf(newSituation,-1));
                }else{
                    currentNode.addChildren(new Leaf(newSituation,0));
                }
            }
            // Pour chaque case libre on crée une situation
            for (int i = 0; i < freeCells; i++){
                newSituation = new TicTacToe_2D(situation);
                newSituation.setCell(newPlayer,i);
                if(newSituation.findSolutionFromCell(emptyCells.get(i))) {
                    if(newPlayer == 'X')
                        currentNode.addChildren(new Leaf(newSituation,1));
                    else
                        currentNode.addChildren(new Leaf(newSituation,-1));
                }
                currentNode.addChildren(new Node(newSituation, newType, Integer.MIN_VALUE, Integer.MAX_VALUE));
            }
            freeCells--;
            for (TreeNode child: currentNode.getChildren()) {
                if(child instanceof Node){
                    TreeNode currentChild = fillTree((Node)child);
                    // Faire l'alpha beta
                    int valChild = currentChild.getValue();
                    if(currentNode.getType().equals("max")) {
                        if(valChild > currentNode.getAlpha()){
                            currentNode.setAlpha(valChild);
                        }
                    }else{
                        if(valChild < currentNode.getBeta()){
                            currentNode.setBeta(valChild);
                        }
                    }
                    if(currentNode.getAlpha() > currentNode.getBeta()){
                        currentNode.setValue(valChild);
                        break;
                    }
                }
            }
        }
        return currentNode;
    }

    public Node getRoot() {
        return root;
    }
}
