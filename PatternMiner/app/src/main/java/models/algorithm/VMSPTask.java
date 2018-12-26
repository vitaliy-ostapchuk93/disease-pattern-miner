package models.algorithm;

import algorithms.sequentialpatterns.spam.AlgoVMSP;
import models.data.DataFile;
import models.data.GroupDataFile;
import models.data.SEQFileType;
import models.data.SeqDataFile;

import java.util.HashMap;
import java.util.Map;

public class VMSPTask extends AbstractAlgorithmTask {

    public VMSPTask() {
        super();
    }


    @Override
    public DataFile miningTask(GroupDataFile groupFile) {
        SeqDataFile input = groupFile.getSeqDataFile(SEQFileType.SEQ_SPMF);
        float minSupp = getMinSup(input);
        DataFile output = input.getOutputResultFile(AlgorithmType.VMSP, minSupp);

        AlgoVMSP algo = new AlgoVMSP();

        algo.setMaximumPatternLength((int) algorithmParameters.getOrDefault("Maximum Pattern Length", 4));
        algo.setMaxGap((int) algorithmParameters.getOrDefault("Gap", 4));

        try {
            algo.runAlgorithm(input.getPath(), output.getPath(), minSupp);
            algo.printStatistics();
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
        params.put("Maximum Pattern Length", 4);
        params.put("Gap", 4);

        return params;
    }

}
