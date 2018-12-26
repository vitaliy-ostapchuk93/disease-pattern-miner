package models.algorithm;

import algorithms.sequentialpatterns.prefixspan.AlgoBIDEPlus;
import models.data.DataFile;
import models.data.GroupDataFile;
import models.data.SEQFileType;
import models.data.SeqDataFile;

import java.util.HashMap;
import java.util.Map;

public class BidePlusTask extends AbstractAlgorithmTask {

    public BidePlusTask() {
        super();
    }

    @Override
    public DataFile miningTask(GroupDataFile groupFile) {
        SeqDataFile input = groupFile.getSeqDataFile(SEQFileType.SEQ_SPMF);
        float minSupp = getMinSup(input);
        DataFile output = input.getOutputResultFile(AlgorithmType.BIDE_PLUS, minSupp);

        AlgoBIDEPlus algorithm = new AlgoBIDEPlus();
        try {
            algorithm.runAlgorithm(input.getPath(), minSupp, output.getPath());
            algorithm.printStatistics();
            return output;
        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
        }
        return null;
    }

    @Override
    Map<String, Object> initialAlgorithmParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put("Minimal Support", 0.01f);
        return params;
    }
}
