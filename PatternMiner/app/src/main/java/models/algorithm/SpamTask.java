package models.algorithm;

import algorithms.sequentialpatterns.spam.AlgoSPAM;
import models.data.DataFile;
import models.data.GroupDataFile;
import models.data.SEQFileType;
import models.data.SeqDataFile;

import java.util.HashMap;
import java.util.Map;

public class SpamTask extends AbstractAlgorithmTask {

    public SpamTask() {
        super();
    }

    @Override
    public DataFile miningTask(GroupDataFile groupFile) {
        SeqDataFile input = groupFile.getSeqDataFile(SEQFileType.SEQ_SPMF);
        float minSupp = getMinSup(input);
        DataFile output = input.getOutputResultFile(AlgorithmType.SPAM, minSupp);

        int minPatternLength = (int) algorithmParameters.getOrDefault("Minimum Pattern Length", 3);
        int maxPatternLength = (int) algorithmParameters.getOrDefault("Maximum Pattern Length", 5);

        AlgoSPAM algo = new AlgoSPAM();

        algo.setMinimumPatternLength(minPatternLength);
        algo.setMaximumPatternLength(maxPatternLength);
        try {
            algo.runAlgorithm(input.getPath(), output.getPath(), minSupp);
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

        return params;
    }

}
