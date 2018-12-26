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
public class MainTestTwoPhaseAlgorithm_saveToMemory {

    public static void main(String[] arg) throws IOException {

        String input = fileToPath("DB_Utility.txt");

        int min_utility = 30;  //

        // Loading the database into memory
        UtilityTransactionDatabaseTP database = new UtilityTransactionDatabaseTP();
        database.loadFile(input);
        database.printDatabase();

        // Applying the Two-Phase algorithm
        AlgoTwoPhase twoPhase = new AlgoTwoPhase();
        ItemsetsTP highUtilityItemsets = twoPhase.runAlgorithm(database, min_utility);

        // print the frequent itemsets found
        highUtilityItemsets.printItemsets(database.size());

        twoPhase.printStats();

    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestTwoPhaseAlgorithm_saveToMemory.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }
}
