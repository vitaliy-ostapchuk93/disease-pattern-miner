package models.data;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GroupDataFile extends DataFile {
    private final static Logger LOGGER = Logger.getLogger(GroupDataFile.class.getName());

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
}
