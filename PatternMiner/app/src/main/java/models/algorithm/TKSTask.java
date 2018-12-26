package models.algorithm;

import algorithms.sequentialpatterns.spam.AlgoTKS;
import models.data.*;

import java.util.HashMap;
import java.util.Map;

public class TKSTask extends AbstractAlgorithmTask {

    public TKSTask() {
        super();
    }


    @Override
    public DataFile miningTask(GroupDataFile groupFile) {
        SeqDataFile input = groupFile.getSeqDataFile(SEQFileType.SEQ_SPMF);
        int k = (int) algorithmParameters.getOrDefault("K", 50);
        ResultsDataFile output = input.getOutputResultFile(AlgorithmType.TKS, k);

        try {
            AlgoTKS algo = new AlgoTKS();

            algo.setMinimumPatternLength((int) algorithmParameters.getOrDefault("Minimum Pattern Length", 0));
            algo.setMaximumPatternLength((int) algorithmParameters.getOrDefault("Maximum Pattern Length", 5));
            algo.setMaxGap((int) algorithmParameters.getOrDefault("Gap", 2));

            algo.runAlgorithm(input.getPath(), output.getPath(), k);

            algo.writeResultTofile(output.getPath());
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

        params.put("K", 50);
        params.put("Minimum Pattern Length", 0);
        params.put("Maximum Pattern Length", 5);
        params.put("Gap", 2);

        return params;
    }

}
