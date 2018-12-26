package models.algorithm;

import algorithms.sequentialpatterns.clospan_AGP.AlgoCloSpan;
import algorithms.sequentialpatterns.clospan_AGP.items.SequenceDatabase;
import algorithms.sequentialpatterns.clospan_AGP.items.creators.AbstractionCreator;
import algorithms.sequentialpatterns.clospan_AGP.items.creators.AbstractionCreator_Qualitative;
import models.data.DataFile;
import models.data.GroupDataFile;
import models.data.SEQFileType;
import models.data.SeqDataFile;

import java.util.Map;

public class CloSpanTask extends AbstractAlgorithmTask {


    public CloSpanTask() {
        super();
    }

    @Override
    public DataFile miningTask(GroupDataFile groupFile) {
        SeqDataFile input = groupFile.getSeqDataFile(SEQFileType.SEQ_SPMF);
        float minSupp = getMinSup(input);
        DataFile output = input.getOutputResultFile(AlgorithmType.CloSpan, minSupp);

        boolean keepPatterns = (boolean) algorithmParameters.getOrDefault("Keep Patterns", true);
        boolean verbose = (boolean) algorithmParameters.getOrDefault("Verbose", true);
        boolean findClosedPatterns = (boolean) algorithmParameters.getOrDefault("Find Closed Patterns", true);
        boolean executePruningMethods = (boolean) algorithmParameters.getOrDefault("Execute Pruning Methods", true);
        boolean outputSequenceIdentifiers = (boolean) algorithmParameters.getOrDefault("Output Sequence Identifiers", false);

        try {
            AbstractionCreator abstractionCreator = AbstractionCreator_Qualitative.getInstance();
            SequenceDatabase sequenceDatabase = new SequenceDatabase();
            sequenceDatabase.loadFile(input.getPath(), minSupp);

            AlgoCloSpan algorithm = new AlgoCloSpan(minSupp, abstractionCreator, findClosedPatterns, executePruningMethods);

            algorithm.runAlgorithm(sequenceDatabase, keepPatterns, verbose, output.getPath(), outputSequenceIdentifiers);

            if (keepPatterns) {
                System.out.println(algorithm.printStatistics());
            }
            return output;
        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
        }

        return null;
    }

    @Override
    public Map<String, Object> initialAlgorithmParameters() {
        return initialSPMFParameters();
    }
}
