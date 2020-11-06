import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    static Scanner sc = new Scanner(System.in);
    /*enum CELL {
        BLANK,
        X,
        O
    }*/

    public static void main(String[] args) {

        System.out.print("n-dimension (2 or 3): ");
        int dimension;

        dimension = chooseDimension();

        ArrayList<Integer> tttList = new ArrayList<Integer>();

        // Read file
        try {
            BufferedReader reader = new BufferedReader(new FileReader("files/2D_Blank.txt"));
            String line;
            while((line = reader.readLine()) != null) {
                List<Integer> intLine = Arrays.stream(line.split(" ")).map(Integer::parseInt).collect(Collectors.toList());
                tttList.addAll(intLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // QUENTIN, TU UTILISES CA
        int[] ttt = tttList.stream().mapToInt(Integer::valueOf).toArray();
    }

    /**
     * Ask user to write dimension he wants to use (2 or 3).
     * @return Selected dimension.
     */
    public static int chooseDimension() {
        int dimension = 2;
        while(true) {
            try {
                dimension = Integer.parseInt(sc.nextLine());
                if (dimension == 2 || dimension == 3)
                    break;
                else
                    throw new NumberFormatException();
                // not positive.
            } catch (NumberFormatException e) {
                // not an integer
                System.out.print("n-dimension (2 or 3): ");
            }
        }
        return dimension;
    }
}
