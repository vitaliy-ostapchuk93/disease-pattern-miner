package models.algorithm;

import algorithms.frequentpatterns.apriori_close.AlgoAprioriClose;
import models.data.DataFile;
import models.data.GroupDataFile;
import models.data.SEQFileType;
import models.data.SeqDataFile;

import java.util.HashMap;
import java.util.Map;

public class AprioriCloseTask extends AbstractAlgorithmTask {

    public AprioriCloseTask() {
        super();
    }

    @Override
    DataFile miningTask(GroupDataFile groupFile) {
        SeqDataFile input = groupFile.getSeqDataFile(SEQFileType.SEQ_SPMF);
        float minSupp = getMinSup(input);
        DataFile output = input.getOutputResultFile(AlgorithmType.AprioriClose, minSupp);

        try {
            AlgoAprioriClose apriori = new AlgoAprioriClose();
            apriori.runAlgorithm(minSupp, input.getPath(), output.getPath());
            apriori.printStats();
            return output;
        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
        }
        return null;
    }

    @Override
    Map<String, Object> initialAlgorithmParameters() {
        Map<String, Object> params = new HashMap<>();

        params.put("Minimal Support", 0.001f);

        return params;
    }
}
