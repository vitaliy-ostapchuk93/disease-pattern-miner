package test;

import algorithms.sequentialpatterns.spam.AlgoVMSP;
import algorithms.sequentialpatterns.spam.PatternVMSP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;
import java.util.TreeSet;


/**
 * Example of how to use the VMSP algorithm in source code.
 *
 * @author Philippe Fournier-Viger
 */
public class MainTestVMSP_saveToMemory {

    public static void main(String[] arg) throws IOException {
        // Load a sequence database
        String input = fileToPath("dataVMSP.txt");
        String output = ".//output.txt";

        // Create an instance of the algorithm
        AlgoVMSP algo = new AlgoVMSP();
        algo.setMaximumPatternLength(8);
        algo.setMaxGap(1);

        // execute the algorithm with minsup = 2 sequences  (50 %)
        List<TreeSet<PatternVMSP>> maxPatterns = algo.runAlgorithm(input, output, 0.8);
        algo.printStatistics();

        // PRINT THE PATTTERNS FOUND
        for (TreeSet<PatternVMSP> tree : maxPatterns) {
            if (tree == null) {
                continue;
            }
            // for each pattern
            for (PatternVMSP pattern : tree) {
                System.out.println(" " + pattern.getPrefix() + "  support : " + pattern.getSupport());
            }
        }
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestVMSP_saveToMemory.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }
}