package models.algorithm;

import algorithms.sequentialpatterns.clasp_AGP.AlgoClaSP;
import algorithms.sequentialpatterns.clasp_AGP.dataStructures.creators.AbstractionCreator;
import algorithms.sequentialpatterns.clasp_AGP.dataStructures.creators.AbstractionCreator_Qualitative;
import algorithms.sequentialpatterns.clasp_AGP.dataStructures.database.SequenceDatabase;
import algorithms.sequentialpatterns.clasp_AGP.idlists.creators.IdListCreator;
import algorithms.sequentialpatterns.clasp_AGP.idlists.creators.IdListCreatorStandard_Map;
import models.data.DataFile;
import models.data.GroupDataFile;
import models.data.SEQFileType;
import models.data.SeqDataFile;

import java.io.IOException;
import java.util.Map;

public class ClasSPTask extends AbstractAlgorithmTask {

    public ClasSPTask() {
        super();
    }

    @Override
    public DataFile miningTask(GroupDataFile groupFile) {
        SeqDataFile input = groupFile.getSeqDataFile(SEQFileType.SEQ_SPMF);
        float minSupp = getMinSup(input);
        DataFile output = input.getOutputResultFile(AlgorithmType.ClaSP, minSupp);

        LOGGER.warning(output.getPath());

        boolean keepPatterns = (boolean) algorithmParameters.getOrDefault("Keep Patterns", true);
        boolean verbose = (boolean) algorithmParameters.getOrDefault("Verbose", true);
        boolean findClosedPatterns = (boolean) algorithmParameters.getOrDefault("Find Closed Patterns", true);
        boolean executePruningMethods = (boolean) algorithmParameters.getOrDefault("Execute Pruning Methods", true);
        boolean outputSequenceIdentifiers = (boolean) algorithmParameters.getOrDefault("Output Sequence Identifiers", false);

        AbstractionCreator abstractionCreator = AbstractionCreator_Qualitative.getInstance();
        IdListCreator idListCreator = IdListCreatorStandard_Map.getInstance();

        SequenceDatabase sequenceDatabase = new SequenceDatabase(abstractionCreator, idListCreator);

        try {
            double relativeSupport = sequenceDatabase.loadFile(input.getPath(), minSupp);
            AlgoClaSP algorithm = new AlgoClaSP(relativeSupport, abstractionCreator, findClosedPatterns, executePruningMethods);

            algorithm.runAlgorithm(sequenceDatabase, keepPatterns, verbose, output.getPath(), outputSequenceIdentifiers);
            algorithm.printStatistics();
            return output;
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        }
        return null;
    }

    @Override
    public Map<String, Object> initialAlgorithmParameters() {
        return initialSPMFParameters();
    }
}
