package test;

import tools.other_dataset_tools.ResizeDatabaseTool;

import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * Class file that shows how to resize a database
 * using the tool called "ResizeDatabaseTool".
 *
 * @author Philippe Fournier-Viger
 */
public class MainTestResizeDatabaseTool {

    public static void main(String[] args) throws Exception {

        // input file path
        String input = fileToPath("DB_UtilityPerHUIs.txt");
        // output file path
        String output = ".//output.txt";

        // percentage of database to use  (e.g. 75 %)
        double percentage = 0.7;

        // Create the tool to resize a database
        ResizeDatabaseTool resizer = new ResizeDatabaseTool();
        // Run the algorithm
        resizer.convert(input, output, percentage);
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestResizeDatabaseTool.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }
}
