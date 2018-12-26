package models.algorithm;

import algorithms.sequentialpatterns.spam.AlgoCMSPAM;
import models.data.DataFile;
import models.data.GroupDataFile;
import models.data.SEQFileType;
import models.data.SeqDataFile;

import java.util.HashMap;
import java.util.Map;

public class CmSpamTask extends AbstractAlgorithmTask {

    public CmSpamTask() {
        super();
    }

    @Override
    public DataFile miningTask(GroupDataFile groupFile) {
        SeqDataFile input = groupFile.getSeqDataFile(SEQFileType.SEQ_SPMF);
        float minSupp = getMinSup(input);
        DataFile output = input.getOutputResultFile(AlgorithmType.CM_SPAM, minSupp);

        boolean outputSequenceIdentifiers = (boolean) algorithmParameters.getOrDefault("Output Sequence Identifiers", false);
        int minPatternLength = (int) algorithmParameters.getOrDefault("Minimum Pattern Length", 3);
        int maxPatternLength = (int) algorithmParameters.getOrDefault("Maximum Pattern Length", 5);
        int maxGap = (int) algorithmParameters.getOrDefault("Max Gap", 2);

        try {
            AlgoCMSPAM algorithm = new AlgoCMSPAM();

            algorithm.setMinimumPatternLength(minPatternLength);
            algorithm.setMaximumPatternLength(maxPatternLength);
            algorithm.setMaxGap(maxGap);

            algorithm.runAlgorithm(input.getPath(), output.getPath(), minSupp, outputSequenceIdentifiers);

            algorithm.printStatistics();
            return output;
        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
        }
        return null;
    }

    @Override
    public Map<String, Object> initialAlgorithmParameters() {
        Map<String, Object> params = new HashMap<>();

        params.put("Minimal Support", 0.01f);
        params.put("Minimum Pattern Length", 3);
        params.put("Maximum Pattern Length", 5);
        params.put("Max Gap", 2);
        params.put("Output Sequence Identifiers", false);

        return params;
    }
}
