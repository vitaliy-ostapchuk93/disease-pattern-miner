package test;

import algorithms.frequentpatterns.two_phase.AlgoTwoPhase;
import algorithms.frequentpatterns.two_phase.ItemsetsTP;
import algorithms.frequentpatterns.two_phase.UtilityTransactionDatabaseTP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * Example of how to use the TWOPhase Algorithm in source code.
 *
 * @author Philippe Fournier-Viger, 2010
 */
public class MainTestTwoPhaseAlgorithm_saveToFile {

    public static void main(String[] arg) throws IOException {

        String input = fileToPath("DB_Utility.txt");
        String output = ".//output.txt";

        int min_utility = 30;  //

        // Loading the database into memory
        UtilityTransactionDatabaseTP database = new UtilityTransactionDatabaseTP();
        database.loadFile(input);

        // Applying the Two-Phase algorithm
        AlgoTwoPhase twoPhase = new AlgoTwoPhase();
        ItemsetsTP highUtilityItemsets = twoPhase.runAlgorithm(database, min_utility);

        highUtilityItemsets.saveResultsToFile(output, database.getTransactions().size());

        twoPhase.printStats();

    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestTwoPhaseAlgorithm_saveToFile.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }
}
