package models.algorithm;

import algorithms.sequentialpatterns.skopus.AlgoSkopus;
import models.data.DataFile;
import models.data.GroupDataFile;
import models.data.SEQFileType;
import models.data.SeqDataFile;

import java.util.HashMap;
import java.util.Map;

public class SkopusTask extends AbstractAlgorithmTask {

    public SkopusTask() {
        super();
    }

    @Override
    public DataFile miningTask(GroupDataFile groupFile) {
        SeqDataFile input = groupFile.getSeqDataFile(SEQFileType.SEQ_SPMF);
        float relSupport = getMinSup(input);
        DataFile output = input.getOutputResultFile(AlgorithmType.Skopus, relSupport);

        boolean useLeverageMeasureInsteadOfSupport = (boolean) algorithmParameters.getOrDefault("Use Leverage Measure Instead Of Support", false);

        boolean showDebugInformation = true;
        boolean useSmoothedValues = (boolean) algorithmParameters.getOrDefault("Use Smoothed Values", false);
        double smoothingCoefficient = (double) algorithmParameters.getOrDefault("Smoothing Coefficient", 0.5);

        int maximumSequentialPatternLength = Integer.MAX_VALUE;
        int k = (int) algorithmParameters.getOrDefault("Maximum Sequential Pattern Length", 10);

        try {
            AlgoSkopus algorithm = new AlgoSkopus();
            algorithm.runAlgorithm(input.getPath(), output.getPath(), useLeverageMeasureInsteadOfSupport, showDebugInformation, useSmoothedValues, smoothingCoefficient, maximumSequentialPatternLength, k);

            algorithm.printStats();
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
        params.put("Use Leverage Measure Instead Of Support", true);
        params.put("Use Smoothed Values", false);
        params.put("Smoothing Coefficient", 0.5);
        params.put("Maximum Sequential Pattern Length", 5);

        return params;
    }

}
