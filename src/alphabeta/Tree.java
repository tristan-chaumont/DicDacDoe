package alphabeta;

import tictactoe.StructureTicTacToe;
import tictactoe.TicTacToe_2D;

import java.util.ArrayList;
import java.util.HashSet;

public class Tree {

    private Node root;
    private int dimension;
    private char player;
    private int freeCells;
    private HashSet<StructureTicTacToe> duplicate;
    static int total = 0;

    public Tree(int dim, char p){
        dimension = dim;
        duplicate = new HashSet<>();
        player = p;
        freeCells = (int) Math.pow(4, dimension);
        if(player == 'X')
        root = new Node(new TicTacToe_2D(), "max", Integer.MIN_VALUE, Integer.MAX_VALUE);
        fillTree(root,freeCells);
    }

    public TreeNode fillTree(Node cn, int tmpfreecell){
        Node currentNode = cn;
        if(dimension == 2){
            if(root == null){
                if(player == 'X')
                    currentNode = new Node(new TicTacToe_2D(), "max", Integer.MIN_VALUE, Integer.MAX_VALUE);
                else
                    currentNode = new Node(new TicTacToe_2D(), "min", Integer.MIN_VALUE, Integer.MAX_VALUE);
            }
            TicTacToe_2D situation = (TicTacToe_2D) currentNode.getSituation();
            ArrayList<Integer> emptyCells = new ArrayList<>(situation.getEmptyCell());
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
            if(tmpfreecell == 1){
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
            for (int i = 0; i < tmpfreecell; i++){
                newSituation = new TicTacToe_2D(situation);
                int pos = emptyCells.get(i);
                newSituation.setCell(newPlayer,pos);
                if(!(this.duplicate.contains(newSituation))){
                    if (newSituation.findSolutionFromCell(pos)) {
                        if (newPlayer == 'X')
                            currentNode.addChildren(new Leaf(newSituation, 1));
                        else
                            currentNode.addChildren(new Leaf(newSituation, -1));
                        if (newPlayer == player)
                            break;
                    }
                    currentNode.addChildren(new Node(newSituation, newType, Integer.MIN_VALUE, Integer.MAX_VALUE));
                    this.duplicate.add(newSituation);
                }
            }
            //total += currentNode.getChildren().size();
            //System.out.println(total);
            tmpfreecell--;
            for (TreeNode child: currentNode.getChildren()) {
                if(child instanceof Node){
                    TreeNode currentChild = fillTree((Node)child,tmpfreecell);
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
