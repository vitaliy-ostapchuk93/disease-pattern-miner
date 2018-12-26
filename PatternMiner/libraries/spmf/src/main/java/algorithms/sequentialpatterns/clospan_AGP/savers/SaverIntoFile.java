package algorithms.sequentialpatterns.clospan_AGP.savers;

import algorithms.sequentialpatterns.clospan_AGP.items.patterns.Pattern;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is an implementation of a class implementing the Saver interface. By
 * means of these lines, the user choose to keep his patterns in a file whose
 * path is given to this class.
 * <p>
 * Copyright Antonio Gomariz Peñalver 2013
 * <p>
 * This file is part of the SPMF DATA MINING SOFTWARE
 * (http://www.philippe-fournier-viger.com/spmf).
 * <p>
 * SPMF is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p>
 * SPMF is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * SPMF. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author agomariz
 */
public class SaverIntoFile implements Saver {

    /**
     * if true, sequence ids will be saved for each pattern
     */
    boolean outputSequenceIdentifiers;
    private BufferedWriter writer = null;
    private String path = null;

    /**
     * Constructor
     *
     * @param outputFilePath            output file path
     * @param outputSequenceIdentifiers if true, sequence ids will be saved for each pattern
     */
    public SaverIntoFile(String outputFilePath, boolean outputSequenceIdentifiers) throws IOException {
        path = outputFilePath;
        this.outputSequenceIdentifiers = outputSequenceIdentifiers;
        writer = new BufferedWriter(new FileWriter(outputFilePath));
    }

    @Override
    public void savePattern(Pattern p) {
        if (writer != null) {
            // create a StringBuilder
            StringBuilder r = new StringBuilder();
            // for each itemset in this sequential pattern
            r.append(p.toStringToFile(outputSequenceIdentifiers));
            try {
                // write the string to the file
                writer.write(r.toString());
                // start a new line
                writer.newLine();
            } catch (IOException ex) {
                Logger.getLogger(SaverIntoFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void finish() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(SaverIntoFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void clear() {
        writer = null;
    }

    @Override
    public String print() {
        return "Content at file " + path;
    }
}
