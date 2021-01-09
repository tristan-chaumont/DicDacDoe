package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Utilities {

    public static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * Récupère le nom de tous les plateaux pour l'interface graphique.
     * @param dir
     *      Répertoire des plateaux. Soit "2D", soit "3D".
     * @param fileConsumer
     *      Opération à effectuer sur chaque fichier.
     */
    public static void fetchFiles(File dir, Consumer<File> fileConsumer) {
        if (dir.isDirectory()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                fetchFiles(file, fileConsumer);
            }
        } else {
            fileConsumer.accept(dir);
        }
    }

    /**
     * Parse un plateau 2D ou 3D pour ajouter chaque case dans une liste d'Integer.
     * @param dimension
     *      Dimension du plateau : 2D ou 3D.
     * @param fileName
     *      Nom du fichier à parse.
     * @return
     *      Liste des cases du plateau.
     */
    public static ArrayList<Integer> parseBoard(int dimension, String fileName) {
        if (dimension == 2) {
            return parse2DBoard(fileName);
        } else {
            return parse3DBoard(fileName);
        }
    }

    private static ArrayList<Integer> parse2DBoard(String fileName) {
        ArrayList<Integer> board = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                List<Integer> intRow = Arrays.stream(line.trim().split("\\s+")).map(Integer::parseInt).collect(Collectors.toList());
                board.addAll(intRow);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return board;
    }

    private static ArrayList<Integer> parse3DBoard(String fileName) {
        ArrayList<Integer> board = new ArrayList<>();
        int n = 4;
        ArrayList<String> rows = new ArrayList<>();
        // on récupère ligne par ligne
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                rows.addAll(Arrays.stream(line.split("\\s+\\|\\s+")).collect(Collectors.toList()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // on insère tout dans le bon ordre
        
        // on saute n itérations dans le tableau pour récupérer la ligne qui correspond au bon étage du morpion
        String[] rowsTab = rows.toArray(new String[0]);
        int boardLength = n * n;
        for (int i = 0; i < boardLength; i += n) {
            List<Integer> intRow = Arrays.stream(rowsTab[i].split("\\s+")).map(Integer::parseInt).collect(Collectors.toList());
            board.addAll(intRow);
            // si i atteint exactement la taille du tableau, on a parcouru le morpion 3D complet, on peut donc arrêter
            if (i == boardLength - 1) {
                break;
            }
            // si on a dépassé la taille du tableau, on a fini l'étage actuel et on peut passer au suivant
            // on doit donc incrémenter i de 1 pour récupérer le prochain étage
            // on veut récupérer la première ligne de l'étage suivant donc on doit modulo n
            // enfin, on retranche n car la prochaine itération du for va incrémenter i de n, ce qui nous ferait manquer une ligne
            if (i + n >= boardLength) {
                i = ((i + 1) % n) - n;
            }
        }
        return board;
    }

    public static char[] boardToTicTacToe(int dim,ArrayList<Integer> board){
        char [] tictactoe = new char[(int) Math.pow(4, dim)];
        int i = 0;
        for(int cell : board){
            switch (cell) {
                case -1:
                    tictactoe[i] = ' ';
                    break;
                case 0:
                    tictactoe[i] = 'O';
                    break;
                case 1:
                    tictactoe[i] = 'X';
                    break;
            }
            i++;
        }
        return tictactoe;
    }
}
