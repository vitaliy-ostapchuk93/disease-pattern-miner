package models.algorithm;

import models.data.DataFile;
import models.data.FileAppendUtils;
import models.data.GroupDataFile;
import models.data.SeqDataFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public abstract class AbstractAlgorithmTask {

    protected static Logger LOGGER;
    protected Map<String, Object> algorithmParameters;

    public AbstractAlgorithmTask() {
        this.LOGGER = Logger.getLogger(this.getClass().getSimpleName());
        this.algorithmParameters = initialAlgorithmParameters();
    }


    abstract DataFile miningTask(GroupDataFile groupFile);

    abstract Map<String, Object> initialAlgorithmParameters();

    public Map<String, Object> getAlgorithmParameters() {
        return this.algorithmParameters;
    }

    protected Map<String, Object> initialSPMFParameters() {
        Map<String, Object> params = new HashMap<>();

        params.put("Minimal Support", 0.001f);
        params.put("Keep Patterns", true);
        params.put("Verbose", true);
        params.put("Find Closed Patterns", true);
        params.put("Execute Pruning Methods", true);
        params.put("Output Sequence Identifiers", false);

        return params;
    }

    protected void writeResultsToOutput(File tmpDir, DataFile outputFile) {
        File[] listOfFiles = tmpDir.listFiles();
        for (File tmpResult : listOfFiles) {
            writeResultTmpToOutput(tmpResult, outputFile);
            tmpResult.delete();
        }
        tmpDir.delete();
    }

    protected void writeResultTmpToOutput(File tmpResult, DataFile outputFile) {
        FileAppendUtils appendUtils = new FileAppendUtils();
        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(tmpResult, "UTF-8");

            while (it.hasNext()) {
                String[] p = it.nextLine().split("\\t");
                String line = p[0] + "-2 #SUP: " + p[1];
                appendUtils.appendToFile(outputFile, line);
            }
            it.close();
            appendUtils.closeAllWriters();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected float getMinSup(SeqDataFile inputFile) {
        float minSup;
        if (algorithmParameters.containsKey("Minimal Support [rel.]")) {
            minSup = (float) algorithmParameters.getOrDefault("Minimal Support [rel.]", 0.01f);
        } else {
            minSup = (float) algorithmParameters.getOrDefault("Minimal Support", 0.01f);
        }

        int seqCount = inputFile.getFileLineCount();
        int minCount = 10;

        if (seqCount * minSup < minCount) {
            minSup = minCount * 1.0f / seqCount;
        }

        return minSup;
    }

}
