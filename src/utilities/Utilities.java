package utilities;

import java.io.File;
import java.util.Objects;
import java.util.function.Consumer;

public class Utilities {

    public static void fetchFiles(File dir, Consumer<File> fileConsumer) {
        if (dir.isDirectory()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                fetchFiles(file, fileConsumer);
            }
        } else {
            fileConsumer.accept(dir);
        }
    }
}
