package AlphaBeta;

import TicTacToe.StructureTicTacToe;
import TicTacToe.TicTacToe_2D;

import java.util.ArrayList;

public class Tree {

    private Node root;
    private int dimension;
    private char player;
    private int freeCells = 16;

    public Tree(int dim, char p){
        root = null;
        dimension = dim;
        player = p;
        fillTree(root);
    }

    public void fillTree(Node cn){
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
            for (int i = 0; i < freeCells; i++){
                newSituation = new TicTacToe_2D(situation);
                newSituation.setCell(newPlayer,i);
                if(newSituation.findSolutionFromCell(emptyCells.get(i))) {
                    if(newPlayer == 'X')
                        currentNode.addChildren(new Leaf(newSituation,1));
                    else
                        currentNode.addChildren(new Leaf(newSituation,-1));
                    if(newPlayer == player)
                        break;
                }
                currentNode.addChildren(new Node(newSituation, newType, Integer.MIN_VALUE, Integer.MAX_VALUE));
            }
            freeCells--;
            for (TreeNode child: currentNode.getChildren()) {
                if(child instanceof Node){
                    fillTree((Node)child);
                }
            }
        }
    }
}