package models.data;

import models.algorithm.AlgorithmType;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

public abstract class DataFile extends File {

    protected final static Logger LOGGER = Logger.getLogger(DataFile.class.getSimpleName());

    protected int linenumber;

    protected Set<DataFile> childFiles;
    protected DataFile parentDataFile;

    public DataFile(String realPath) {
        super(realPath);
        this.childFiles = new HashSet<>();
    }

    public ResultsDataFile getOutputResultFile(AlgorithmType type, float minSup) {
        ResultsDataFile out = new ResultsDataFile(getPathAsOutput(type, minSup));

        if (!out.exists()) {
            try {
                out.getParentFile().mkdirs();
                out.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.childFiles.add(out);
        out.setParentDataFile(this);

        LOGGER.info("Creating RESULT child file for " + type.name() + " > " + out.getName());

        return out;
    }

    public String getPathAsOutput(AlgorithmType algorithmType, float minSup) {
        return getPathAsOutput(algorithmType.name() + "_" + minSup).replace("_seqfsm", "").replace("_seqlash", "").replace("_seq", "");
    }

    public String getPathAsOutput(String suffix) {
        String name = getName().replace(".", "_" + suffix + ".");
        File outDir = new File(this.getParentFile().getParent().replace("input", "output"));
        File out = new File(outDir.getAbsolutePath() + File.separator + name);
        return out.getPath();
    }

    public int getLengthInMB() {
        return (int) (this.length() / 1024 / 1024);
    }

    public Set<DataFile> getChildFiles() {
        return childFiles;
    }

    public List<DataFile> getSortedChildFiles() {

        List<DataFile> files = new ArrayList<>(childFiles);
        files.sort(Comparator.comparing(File::getName));

        return files;
    }

    public int getFileLineCount() {
        if (linenumber == 0) {
            try {
                if (this.exists()) {

                    FileReader fr = new FileReader(this);
                    LineNumberReader lnr = new LineNumberReader(fr);

                    while (lnr.readLine() != null) {
                        linenumber++;
                    }

                    lnr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return linenumber;
    }

    private String getGroupFromName() {
        if (getName().contains("_")) {
            return getName().split("_")[1].toUpperCase();
        } else {
            return null;
        }
    }

    public Gender getGenderGroup() {
        if (getGroupFromName().contains("M")) {
            return Gender.MALE;
        }
        if (getGroupFromName().contains("F")) {
            return Gender.FEMALE;
        }
        return null;
    }

    public int getAgeGroup() {
        for (int i = 0; i < 10; i++) {
            if (getGroupFromName().contains(String.valueOf(i))) {
                return i;
            }
        }
        return -1;
    }

    public GenderAgeGroup getGendederAgeGroup() {
        return new GenderAgeGroup(getGenderGroup(), getAgeGroup());
    }

    public DataFile getParentDataFile() {
        return parentDataFile;
    }

    public void setParentDataFile(DataFile parentDataFile) {
        this.parentDataFile = parentDataFile;
    }

    public void makeChild(DataFile childFile) {
        this.childFiles.add(childFile);
        childFile.setParentDataFile(this);
    }

    public String getStats() {
        return getFileStats();
    }

    protected String getFileStats() {
        String separator = ", ";

        String stats = getName() + " [";

        stats += "Type=" + this.getClass().getSimpleName() + separator;
        stats += "LineCount=" + getFileLineCount() + separator;
        stats += "SizeInBytes=" + length();

        stats += "]";

        return stats;
    }

    protected String createStatistics(List<Integer> integerList) {
        IntSummaryStatistics statistics = integerList.stream().mapToInt((x) -> x).summaryStatistics();
        return statistics.toString();
    }

    public File getTempDir() throws IOException {
        Path tmp = Files.createTempDirectory(Paths.get(getParent()), "tmp");
        LOGGER.info("Created tmp file > " + tmp);
        return tmp.toFile();
    }

    public abstract String getPathToChildren();

    public abstract DataFile createChildFile(String path);
}