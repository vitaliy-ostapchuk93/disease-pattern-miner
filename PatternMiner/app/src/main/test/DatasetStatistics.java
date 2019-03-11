import models.data.*;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatasetStatistics {

    private final static Logger LOGGER = Logger.getLogger(TestFileBuilder.class.getName());

    public static void main(String[] arg) {
        LOGGER.setLevel(Level.INFO);
        //String path = "C:\\Users\\vital\\Desktop\\csv\\groups\\";
        String input = "C:\\Users\\vital\\Desktop\\csv\\all\\testDiseasesOfInterest.csv";

        MainDataFile mainDataFile = new MainDataFile(input);
        mainDataFile.splitIntoSortedGroups();

        HashSet<String> patients = new HashSet<>();
        HashSet<ICDCode> codes = new HashSet<>();

        try {
            //for (Path groupFile: Files.newDirectoryStream(Paths.get(path))) {
            //   GroupDataFile groupDataFile = new GroupDataFile(groupFile.toString());
            for (DataFile dataFile : mainDataFile.getChildFiles()) {
                GroupDataFile groupDataFile = (GroupDataFile) dataFile;
                LOGGER.info(groupDataFile.getStats());

                Set<String> groupPatients = groupDataFile.getDistinctPatients();
                LOGGER.info("Distinct Patients: " + groupPatients.size());
                patients.addAll(groupPatients);

                Set<ICDCode> groupCodes = groupDataFile.getDistinctCodes();
                LOGGER.info("Distinct Codes: " + groupCodes.size());
                codes.addAll(groupCodes);

                SeqDataFile seqDataFile = groupDataFile.getSeqDataFile(SEQFileType.SEQ_SPMF);
                LOGGER.info(seqDataFile.getStats());
            }

            LOGGER.info("All Patients Count: " + patients.size());
            LOGGER.info("All Codes Count: " + codes.size());
        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
        }
    }
}
