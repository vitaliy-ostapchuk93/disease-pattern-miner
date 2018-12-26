package test;

import algorithms.clustering.text_clusterer.TextClusterAlgo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * Example of how to use the Text Clusterer from the source code
 *
 * @author Sabarish Raghu (Copyright 2015)
 */
public class MainTestTextClusterer {

    public static void main(String[] arg) throws IOException {

        String input = fileToPath("input_text_clustering.txt");
        String output = "output.txt";
        boolean performStemming = true;
        boolean removeStopWords = true;

        TextClusterAlgo algo = new TextClusterAlgo();
        algo.runAlgorithm(input, output, performStemming, removeStopWords);
        algo.printStatistics();
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestTextClusterer.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }
}
