package models.algorithm;

import driver.GsmDriver;
import models.data.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LashTask extends AbstractAlgorithmTask {

    public LashTask() {
        super();
    }

    @Override
    public DataFile miningTask(GroupDataFile groupFile) {
        SeqDataFile input_seq = groupFile.getSeqDataFile(SEQFileType.SEQ_LASH);
        DataFile input_hierarchy = groupFile.getSeqDataFile(SEQFileType.HIR_LASH);

        float relSupport = getMinSup(input_seq);
        ResultsDataFile output = input_seq.getOutputResultFile(AlgorithmType.LASH, relSupport);

        int support = Math.round(input_seq.getFileLineCount() * relSupport);
        int gamma = (int) algorithmParameters.getOrDefault("Gamma (~gap)", 2);
        int lambda = (int) algorithmParameters.getOrDefault("Lambda (~maximum length)", 5);

        try {
            File tmp = output.getTempDir();
            String args = "-i " + input_seq.getPath() + " -H " + input_hierarchy.getPath() + " -o " + tmp.getPath() + " -s " + support + " -g " + gamma + " -l " + lambda + " -m s";

            LOGGER.info("Running FSM-LASH Job, args = [" + args + "]");
            GsmDriver.main(args.split(" "));
            writeResultsToOutput(tmp, output);
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
        params.put("Gamma (~gap)", 2);
        params.put("Lambda (~maximum length)", 5);

        return params;
    }
}
