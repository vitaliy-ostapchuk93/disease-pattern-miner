package test;

import algorithms.frequentpatterns.cfpgrowth.AlgoCFPGrowth;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * Example of how to use the CFPGrowth++ algorithm, from the source code.
 */
public class MainTestCFPGrowth_saveToFile {

    public static void main(String[] arg) throws FileNotFoundException,
            IOException {
        String database = fileToPath("contextCFPGrowth.txt");
        String output = ".//output.txt";
        String MISfile = fileToPath("MIS.txt");

        // Applying the CFPGROWTH algorithmMainTestFPGrowth.java
        AlgoCFPGrowth algo = new AlgoCFPGrowth();
        algo.runAlgorithm(database, output, MISfile);
        algo.printStats();
    }

    public static String fileToPath(String filename)
            throws UnsupportedEncodingException {
        URL url = MainTestCFPGrowth_saveToFile.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }
}
