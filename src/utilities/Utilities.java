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

    public static ArrayList<Integer> parse2DBoard(String fileName) {
        ArrayList<Integer> board = new ArrayList<Integer>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = reader.readLine()) != null) {
                List<Integer> intLine = Arrays.stream(line.trim().split("\\s+")).map(Integer::parseInt).collect(Collectors.toList());
                board.addAll(intLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return board;
    }

    public static ArrayList<Integer> parse3DBoard(String fileName) {
        ArrayList<Integer> board = new ArrayList<Integer>();
        //TODO
        return board;
    }
}
