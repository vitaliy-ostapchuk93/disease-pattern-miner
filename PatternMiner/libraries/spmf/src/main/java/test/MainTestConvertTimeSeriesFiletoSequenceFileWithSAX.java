package test;

import algorithms.timeseries.TimeSeries;
import algorithms.timeseries.reader_writer.AlgoTimeSeriesReader;
import algorithms.timeseries.sax.AlgoConvertTimeSeriesFileToSequencesWithSAX;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;

/**
 * Example of how to use SAX algorithm for converting a time series from the source code.
 *
 * @author Philippe Fournier-Viger, 2016.
 */
public class MainTestConvertTimeSeriesFiletoSequenceFileWithSAX {

    public static void main(String[] arg) throws IOException {

        // the input file
        String input = fileToPath("contextSAX.txt");

        // Parameters of the algorithm
        String separator = ",";

        // Applying the  algorithm
        AlgoTimeSeriesReader reader = new AlgoTimeSeriesReader();
        List<TimeSeries> timeSeries = reader.runAlgorithm(input, separator);
        reader.printStats();

        // the output file
        String output = ".//output.txt";

        // Parameters of the algorithm
        int numberOfSegments = 8;
        int numberOfSymbols = 4;

        // Set this variable to true to not apply PAA before SAX
        boolean deactivatePAA = false;

        // Applying the  algorithm
        AlgoConvertTimeSeriesFileToSequencesWithSAX algorithm = new AlgoConvertTimeSeriesFileToSequencesWithSAX();
        algorithm.runAlgorithm(timeSeries, output, numberOfSegments, numberOfSymbols, deactivatePAA);
        algorithm.printStats();
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestConvertTimeSeriesFiletoSequenceFileWithSAX.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }
}
