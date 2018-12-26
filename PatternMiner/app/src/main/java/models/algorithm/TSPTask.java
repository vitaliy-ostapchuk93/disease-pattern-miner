package models.algorithm;

import algorithms.sequentialpatterns.BIDE_and_prefixspan.AlgoTSP_nonClosed;
import input.sequence_database_list_integers.SequenceDatabase;
import models.data.DataFile;
import models.data.GroupDataFile;
import models.data.SEQFileType;

import java.util.HashMap;
import java.util.Map;

public class TSPTask extends AbstractAlgorithmTask {

    public TSPTask() {
        super();
    }


    @Override
    public DataFile miningTask(GroupDataFile groupFile) {
        DataFile input = groupFile.getSeqDataFile(SEQFileType.SEQ_SPMF);
        int k = (int) algorithmParameters.getOrDefault("K", 50);
        DataFile output = input.getOutputResultFile(AlgorithmType.TSP, k);

        try {
            SequenceDatabase sequenceDatabase = new SequenceDatabase();
            sequenceDatabase.loadFile(input.getPath());

            AlgoTSP_nonClosed algo = new AlgoTSP_nonClosed();
            algo.runAlgorithm(sequenceDatabase, k);

            algo.writeResultTofile(output.getPath());
            algo.printStatistics(sequenceDatabase.size());

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

        return params;
    }
}
