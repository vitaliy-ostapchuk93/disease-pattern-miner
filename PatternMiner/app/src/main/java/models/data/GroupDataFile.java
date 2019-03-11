package models.data;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

                    if (chunkLines.size() >= 1000000) {
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

    public void createStreamedGroupFile() {
        File streamed = new File(getPath().replace("_sorted", "_streamed"));
        FileAppendUtils appendUtils = new FileAppendUtils();

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
                    appendUtils.appendToFile(streamed, sequence.getFormatedSeqStreamed());
                    sequence = new ICDSequence(p[0]);
                }

                //same sequence
                if (p.length >= 2) {
                    sequence.addDiagnoses(p[1], Arrays.copyOfRange(p, 2, p.length));
                }
            }
            it.close();
            appendUtils.closeAllWriters();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getDistinctCodesCount() {
        return getDistinctPatients().size();
    }

    public Set<String> getDistinctPatients() {
        Set<String> patientIDs = new HashSet<>();

        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(this, "UTF-8");
            while (it.hasNext()) {
                String line = it.nextLine();
                String patientID = line.split(COMMA)[0];
                patientIDs.add(patientID);
            }
            it.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patientIDs;
    }

    public int getDistinctPatientsCount() {
        return getDistinctCodes().size();
    }

    public Set<ICDCode> getDistinctCodes() {
        Set<ICDCode> codes = new HashSet<>();

        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(this, "UTF-8");
            while (it.hasNext()) {
                String line = it.nextLine();
                String[] p = line.split(COMMA);
                String[] icds = Arrays.copyOfRange(p, 2, p.length);
                codes.addAll(Arrays.stream(icds).map(ICDCode::new).collect(Collectors.toSet()));
            }
            it.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return codes;
    }

    public int validSequenceCount() {
        int count = 0;

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
                    if (sequence.getFilteredDiagnosesCount() > 2) {
                        count++;
                    }
                    sequence = new ICDSequence(p[0]);
                }

                //same sequence
                if (p.length >= 2) {
                    sequence.addDiagnoses(p[1], Arrays.copyOfRange(p, 2, p.length));
                }
            }
            it.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return count;
    }
}
