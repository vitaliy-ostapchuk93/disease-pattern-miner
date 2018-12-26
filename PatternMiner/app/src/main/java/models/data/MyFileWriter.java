package models.data;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class MyFileWriter {

    private BiMap<String, BufferedWriter> writerBiMap;

    public MyFileWriter() {
        writerBiMap = HashBiMap.create();
    }


    /**
     * Append by file.
     *
     * @param output
     * @param line
     */
    public void appendToFile(File output, String line) {
        appendToFile(output.getPath(), line);
    }


    /**
     * Append a line to specified file.
     *
     * @param outputPath Path to the file to which append.
     * @param line       Line which you want to append.
     */
    public void appendToFile(String outputPath, String line) {
        if (!writerBiMap.containsKey(outputPath)) {
            writerBiMap.put(outputPath, createWriter(outputPath));
        }

        BufferedWriter writer = writerBiMap.get(outputPath);

        try {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates new Buffed writer for the given output filePath.
     *
     * @param filePath Path to the written file.
     * @return BufferedWriter which will keep stream open.
     */
    public BufferedWriter createWriter(String filePath) {
        try {
            File file = new File(filePath);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            // true = append file
            FileWriter fileWriter = new FileWriter(file.getPath(), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            return bufferedWriter;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Close all open writers.
     */
    public void closeAllWriters() {
        for (BiMap.Entry<String, BufferedWriter> entry : writerBiMap.entrySet()) {
            try {
                entry.getValue().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writerBiMap.clear();
    }

    public Set<String> getPathsSet() {
        return writerBiMap.keySet();
    }
}
