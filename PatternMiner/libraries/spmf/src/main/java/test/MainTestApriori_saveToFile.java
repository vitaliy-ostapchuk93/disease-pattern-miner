package test;

import algorithms.frequentpatterns.apriori.AlgoApriori;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * Example of how to use APRIORI algorithm from the source code.
 *
 * @author Philippe Fournier-Viger (Copyright 2008)
 */
public class MainTestApriori_saveToFile {

    public static void main(String[] arg) throws IOException {

        String input = "C:\\Users\\vital\\Desktop\\Auslandssemester\\disease-pattern-miner\\PatternMiner\\libraries\\spmf\\src\\main\\java\\test\\contextPasquier99.txt";
        String output = "C:\\Users\\vital\\Desktop\\Auslandssemester\\disease-pattern-miner\\PatternMiner\\libraries\\spmf\\src\\main\\java\\test\\output_a.txt";  // the path for saving the frequent itemsets found

        double minsup = 0.5; // means a minsup of 2 transaction (we used a relative support)

        // Applying the Apriori algorithm
        AlgoApriori algo = new AlgoApriori();

        // Uncomment the following line to set the maximum pattern length (number of items per itemset)
//		algo.setMaximumPatternLength(3);

        algo.runAlgorithm(minsup, input, output);
        algo.printStats();
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestApriori_saveToFile.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }
}
