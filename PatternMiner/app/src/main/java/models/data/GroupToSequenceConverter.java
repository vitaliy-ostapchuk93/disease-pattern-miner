package models.data;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GroupToSequenceConverter {

    private final static Logger LOGGER = Logger.getLogger(GroupToSequenceConverter.class.getName());
    private static final String COMMA = ",";
    private FileAppendUtils appendUtils;


    public GroupToSequenceConverter() {
        this.appendUtils = new FileAppendUtils();

        LOGGER.setLevel(Level.INFO);
    }

    public SeqDataFile transformToSEQofType(DataFile input, SEQFileType type) {
        String outputName = null;
        if (type == SEQFileType.SEQ_SPMF) {
            outputName = input.getName().replace("_sorted.csv", "_seq.txt");
        }
        if (type == SEQFileType.SEQ_MGFSM) {
            outputName = input.getName().replace("_sorted.csv", "_seqfsm.txt");
        }
        if (type == SEQFileType.SEQ_LASH) {
            outputName = input.getName().replace("_sorted.csv", "_seqlash.txt");
        }
        if (type == SEQFileType.HIR_LASH) {
            outputName = input.getName().replace("_sorted.csv", "_hierarchy.txt");
        }

        LOGGER.info("Transforming " + input.getName() + " > " + outputName + " of type " + type);

        String dirPath = input.getParentFile().getParentFile().getParent() + File.separator + "converted" + File.separator;
        File output = new File(dirPath + outputName);

        if (!output.exists()) {
            if (type == SEQFileType.HIR_LASH) {
                transformToHierarchy(input, output);
            } else {
                transformToSEQ(input, output, type);
            }
        }

        SeqDataFile file = new SeqDataFile(output.getPath());
        input.makeChild(file);

        LOGGER.info("Finished transform successfully.");

        return file;
    }

    private void transformToHierarchy(DataFile input, File output) {
        Set<ICDCode> icds = createICDCodesSet(input);

        try {
            for (ICDCode code : icds) {
                String hierarchy = code.getSmallCode() + "\t" + code.getGroup().ordinal();
                appendUtils.appendToFile(output, hierarchy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            appendUtils.closeAllWriters();
        }
    }

    private Set<ICDCode> createICDCodesSet(DataFile input) {
        Set<ICDCode> codes = new HashSet<>();

        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(input, "UTF-8");

            ICDSequence sequence = null;

            while (it.hasNext()) {
                String[] p = it.nextLine().split(COMMA);

                //first sequence
                if (sequence == null) {
                    sequence = new ICDSequence(p[0]);
                }

                //different sequence id
                if (!p[0].equals(sequence.getId())) {
                    codes.addAll(sequence.getAllIcdCodes());
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
        } finally {
            appendUtils.closeAllWriters();
        }
        return codes;
    }


    public void transformToSEQ(File input, File output, SEQFileType type) {
        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(input, "UTF-8");

            ICDSequence sequence = null;

            while (it.hasNext()) {
                String[] p = it.nextLine().split(COMMA);

                //first sequence
                if (sequence == null) {
                    sequence = new ICDSequence(p[0]);
                }

                //different sequence id
                if (!p[0].equals(sequence.getId())) {
                    writeSequenceToFile(output, sequence, type);
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
        } finally {
            appendUtils.closeAllWriters();
        }
    }

    private void writeSequenceToFile(File output, ICDSequence sequence, SEQFileType type) {
        if (sequence.getFilteredDiagnosesCount() > 2) {                                                         //  only ones which have 3 or more entries and filtered by groups
            String formattedSeq = "";
            switch (type) {
                case SEQ_SPMF:
                    formattedSeq = sequence.getFormatedSeqSPMF();
                    break;
                case SEQ_MGFSM:
                    formattedSeq = sequence.getFormatedSeqMGSF();
                    break;
                case SEQ_LASH:
                    formattedSeq = sequence.getFormatedSeqLASH();
                    break;
            }

            if (!formattedSeq.isEmpty()) {
                appendUtils.appendToFile(output, formattedSeq);
            }
        }
    }


}
