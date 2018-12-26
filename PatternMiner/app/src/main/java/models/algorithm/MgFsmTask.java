package models.algorithm;

import driver.FsmDriver;
import models.data.DataFile;
import models.data.GroupDataFile;
import models.data.SEQFileType;
import models.data.SeqDataFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MgFsmTask extends AbstractAlgorithmTask {


    public MgFsmTask() {
        super();
    }

    @Override
    public DataFile miningTask(GroupDataFile groupFile) {
        SeqDataFile input = groupFile.getSeqDataFile(SEQFileType.SEQ_MGFSM);
        float relSupport = getMinSup(input);
        DataFile output = input.getOutputResultFile(AlgorithmType.MG_FSM, relSupport);

        int support = Math.round(input.getFileLineCount() * relSupport);
        int gamma = (int) algorithmParameters.getOrDefault("Gamma (~gap)", 2);
        int lambda = (int) algorithmParameters.getOrDefault("Lambda (~maximum length)", 5);

        try {
            File tmp = output.getTempDir();
            String args = "-i " + input.getPath() + " -o " + tmp.getPath() + " -s " + support + " -g " + gamma + " -l " + lambda + " -m s";

            LOGGER.info("Running FSM-MGFSM Job, args = [" + args + "]");

            FsmDriver.main(args.split(" "));
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
