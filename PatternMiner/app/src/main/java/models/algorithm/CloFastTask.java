package models.algorithm;

import algorithms.sequentialpatterns.clofast.AlgoCloFast;
import models.data.DataFile;
import models.data.GroupDataFile;
import models.data.SEQFileType;
import models.data.SeqDataFile;

import java.util.HashMap;
import java.util.Map;

public class CloFastTask extends AbstractAlgorithmTask {

    public CloFastTask() {
        super();
    }

    @Override
    public DataFile miningTask(GroupDataFile groupFile) {
        SeqDataFile input = groupFile.getSeqDataFile(SEQFileType.SEQ_SPMF);
        float minSupp = getMinSup(input);
        DataFile output = input.getOutputResultFile(AlgorithmType.CloFAST, minSupp);


        AlgoCloFast algorithm = new AlgoCloFast();
        try {
            algorithm.runAlgorithm(input.getPath(), output.getPath(), minSupp);
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

        return params;
    }
}
