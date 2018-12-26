package models.algorithm;

import algorithms.sequentialpatterns.BIDE_and_prefixspan.AlgoMaxSP;
import input.sequence_database_list_integers.SequenceDatabase;
import models.data.DataFile;
import models.data.GroupDataFile;
import models.data.SEQFileType;
import models.data.SeqDataFile;

import java.util.HashMap;
import java.util.Map;

public class MaxSPTask extends AbstractAlgorithmTask {

    public MaxSPTask() {
        super();
    }


    @Override
    public DataFile miningTask(GroupDataFile groupFile) {
        SeqDataFile input = groupFile.getSeqDataFile(SEQFileType.SEQ_SPMF);
        float relSupport = getMinSup(input);
        DataFile output = input.getOutputResultFile(AlgorithmType.MaxSP, relSupport);

        int support = Math.round(input.getFileLineCount() * relSupport);

        try {
            SequenceDatabase sequenceDatabase = new SequenceDatabase();
            sequenceDatabase.loadFile(input.getPath());

            AlgoMaxSP algo = new AlgoMaxSP();

            algo.runAlgorithm(sequenceDatabase, output.getPath(), support);
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

        params.put("Minimal Support [rel.]", 0.01f);

        return params;
    }

}
