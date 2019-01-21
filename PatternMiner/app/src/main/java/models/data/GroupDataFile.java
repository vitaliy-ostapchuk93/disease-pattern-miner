package models.data;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GroupDataFile extends DataFile {
    private final static Logger LOGGER = Logger.getLogger(GroupDataFile.class.getName());
    private static final String COMMA = ",";

    public final static String DIR_PATH = "/resources/datasets/input/csv/groups/";
    protected boolean selected;


    public GroupDataFile(String realPath) {
        super(realPath);
        this.selected = false;

        LOGGER.setLevel(Level.INFO);
    }

    @Override
    public String getPathToChildren() {
        return SeqDataFile.DIR_PATH;
    }

    @Override
    public DataFile createChildFile(String path) {
        return new SeqDataFile(path);
    }

    public SeqDataFile getSeqDataFile(SEQFileType type) {
        LOGGER.info("Getting SEQ-File of type " + type.name() + " from parent " + this.getName());

        for (DataFile file : childFiles) {
            if (file instanceof SeqDataFile) {
                if (type == SEQFileType.SEQ_SPMF && file.getName().contains("_seq.txt")) {
                    return (SeqDataFile) file;
                }
                if (type == SEQFileType.SEQ_MGFSM && file.getName().contains("_seqfsm.txt")) {
                    return (SeqDataFile) file;
                }
                if (type == SEQFileType.SEQ_LASH && file.getName().contains("_seqlash.txt")) {
                    return (SeqDataFile) file;
                }
                if (type == SEQFileType.HIR_LASH && file.getName().contains("_hierarchy.txt")) {
                    return (SeqDataFile) file;
                }
            }
        }

        GroupToSequenceConverter converter = new GroupToSequenceConverter();
        SeqDataFile seqDataFile = converter.transformToSEQofType(this, type);

        LOGGER.info("Seq file is > " + seqDataFile.getName());
        return seqDataFile;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void toggleSelected() {
        this.selected = !selected;
    }

    public ArrayList<String> getChunks() {
        ArrayList<String> paths = new ArrayList<>();
        if (getLengthInMB() > 50) {
            File chunkDir = new File(this.getPath().replace(".csv", ""));
            if (chunkDir.exists() && chunkDir.isDirectory() && chunkDir.listFiles().length != 0) {
                for (File chunkFile : chunkDir.listFiles()) {
                    paths.add(chunkFile.getPath());
                }
            } else {
                chunkDir.mkdirs();
                return splitFileInChunks();
            }
        } else {
            paths.add(getPath());
        }
        return paths;
    }

    private ArrayList<String> splitFileInChunks() {
        ArrayList<String> chunkPaths = new ArrayList<>();
        ArrayList<String> chunkLines = new ArrayList<>();
        int counter = 0;

        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(this, "UTF-8");

            ICDSequence sequence = null;
            while (it.hasNext()) {
                String line = it.nextLine();
                String[] p = line.split(COMMA);

                //first sequence
                if (sequence == null) {
                    sequence = new ICDSequence(p[0]);
                }

                //different sequence id
                if (!p[0].equals(sequence.getId())) {
                    sequence = new ICDSequence(p[0]);

                    if (chunkLines.size() >= 500000) {
                        chunkPaths.add(saveChunkAsFile(chunkLines, counter));
                        chunkLines = new ArrayList<>();
                        counter++;
                    }
                }

                //same sequence
                if (p.length >= 2) {
                    sequence.addDiagnoses(p[1], Arrays.copyOfRange(p, 2, p.length));
                    chunkLines.add(line);
                }
            }
            it.close();
            chunkPaths.add(saveChunkAsFile(chunkLines, counter));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return chunkPaths;
    }

    private String saveChunkAsFile(ArrayList<String> chunkLines, int counter) {
        File chunkFile = new File(this.getPath().replace(".csv", File.separator) + this.getName().replace(".csv", "_c" + counter + ".csv"));
        FileAppendUtils appendUtils = new FileAppendUtils();

        for (String chunkLine : chunkLines) {
            appendUtils.appendToFile(chunkFile, chunkLine);
        }

        appendUtils.closeAllWriters();

        return chunkFile.getPath();
    }
}
