package models.data;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainToGroupConverter {

    private final static Logger LOGGER = Logger.getLogger(MainToGroupConverter.class.getName());

    private FileAppendUtils appendUtils;

    public MainToGroupConverter() {
        this.appendUtils = new FileAppendUtils();

        LOGGER.setLevel(Level.INFO);
    }


    /**
     * Split the given main DataFile into group files.
     *
     * @param mainFile Main file which is splitted.
     */
    public void splitIntoSortedGroups(DataFile mainFile) {
        Set<String> paths = splitIntoGroups(mainFile);
        Set<DataFile> sortedFiles = sortGivenGroupFiles(paths);
        mainFile.getChildFiles().addAll(sortedFiles);
    }


    /**
     * Split the given main file into smaller group files.
     *
     * @param mainFile The Main DataFile which is slitted.
     * @return Returns a set of paths to the created group files.
     */
    private Set<String> splitIntoGroups(DataFile mainFile) {
        Set<String> paths = new HashSet<>();

        String groupPath = mainFile.getParent().replace("all", "groups") + File.separator;
        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(mainFile, "UTF-8");

            //split
            while (it.hasNext()) {
                String line = it.nextLine();
                String group = line.substring(0, 2);

                appendUtils.appendToFile(groupPath + "tmp_" + mainFile.getName().split(".csv")[0].replace("test", "") + "_" + group + ".csv", line.substring(3));
            }

            paths.addAll(appendUtils.getPathsSet());
            it.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            appendUtils.closeAllWriters();
        }
        return paths;
    }

    /**
     * Sort given group files.
     *
     * @param pathsOfUnsorted Paths to the different group files.
     * @return Returns a Set of group DataFiles which are sorted.
     */
    private Set<DataFile> sortGivenGroupFiles(Set<String> pathsOfUnsorted) {
        Set<DataFile> sortedFiles = new ConcurrentSkipListSet<>();

        for (String filePath : pathsOfUnsorted) {
            File sorted = sortGivenGroupFile(new File(filePath));

            GroupDataFile dataFile = new GroupDataFile(sorted.getPath());
            dataFile.getChunks();
            dataFile.setSelected(true);

            sortedFiles.add(dataFile);
        }


        return sortedFiles;
    }


    /**
     * Create some temporal directory which will be deleted again on exit.
     *
     * @param path Path where to create the directory.
     */
    private File createTmpDir(String path) {
        try {
            Path tmpDir = Files.createTempDirectory(Paths.get(path), "tmp");
            LOGGER.info("Created tmp file > " + tmpDir);

            return tmpDir.toFile();
        } catch (IOException e) {
            LOGGER.warning("Couldnt create tmpdir");
            LOGGER.warning(e.getMessage());
        }
        return null;
    }


    /**
     * Sort a single group file.
     *
     * @param input Input group file.
     */
    public File sortGivenGroupFile(File input) {
        File sorted = new File(input.getPath().replace(".csv", "_sorted.csv").replace("tmp_", ""));

        LOGGER.info("Sort " + input.getName() + " > " + sorted.getName());

        File tmp = createTmpDir(input.getParent());
        splitGroupIntoPatients(input, tmp);

        input.delete();

        mergeBackFromDir(tmp, sorted);

        tmp.delete();

        return sorted;
    }


    /**
     * Split the group in small files of patients with similar id's (suffix = first 2 characters of PID).
     *
     * @param input The group file which is splitted.
     * @param tmp   A tmp directory where the new splitted files will be stored.
     */
    public void splitGroupIntoPatients(File input, File tmp) {
        LOGGER.info("Read throug " + input.getName() + " and try split it.");
        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(input, "UTF-8");

            while (it.hasNext()) {
                String line = it.nextLine();
                String suffix = line.substring(0, 2);

                appendUtils.appendToFile(tmp.getPath() + File.separator + suffix + ".csv", line);
            }
            it.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            appendUtils.closeAllWriters();
        }
    }


    /**
     * Merge back from all tmp directory.
     *
     * @param tmp    Tmp directory.
     * @param output The output file.
     */
    private void mergeBackFromDir(File tmp, File output) {
        for (File tmpF : tmp.listFiles()) {
            LOGGER.info("Trying sort & merging back: " + tmpF.getPath());
            mergeBackFromSingleFile(tmpF, output);
        }
    }

    /**
     * Sort and merge back.
     *
     * @param tmpF   Temporary single file.
     * @param output Output file to which merge back.
     */
    private void mergeBackFromSingleFile(File tmpF, File output) {
        List<String> lines;
        try {
            lines = FileUtils.readLines(tmpF, "UTF-8");
            Collections.sort(lines);

            for (String line : lines) {
                appendUtils.appendToFile(output.getPath(), line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        tmpF.delete();
    }


}
