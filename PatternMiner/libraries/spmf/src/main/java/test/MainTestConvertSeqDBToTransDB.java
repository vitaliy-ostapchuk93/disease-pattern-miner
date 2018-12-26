package test;

import tools.dataset_converter.Formats;
import tools.dataset_converter.TransactionDatabaseConverter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * Example of how to convert a sequence database in SPMF format to
 * a transaction database in SPMF format.
 */
public class MainTestConvertSeqDBToTransDB {

    public static void main(String[] arg) throws IOException {

        String inputFile = fileToPath("contextPrefixspan.txt");
        String outputFile = ".//output.txt";
        Formats inputFileformat = Formats.SPMF_SEQUENCE_DB;
        int sequenceCount = Integer.MAX_VALUE;

        TransactionDatabaseConverter converter = new TransactionDatabaseConverter();
        converter.convert(inputFile, outputFile, inputFileformat, sequenceCount);
    }


    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestConvertSeqDBToTransDB.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }
}
