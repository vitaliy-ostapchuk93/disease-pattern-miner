package algorithms.sequentialpatterns.spade_spam_AGP.savers;

import algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.patterns.Pattern;

import java.util.Collection;

/**
 * This is the definition of a interface in order to decide where the user wants
 * to keep the patterns. The implementer classes will refer to the place for
 * keeping them
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
public interface Saver {

    /**
     * Save patterns to file
     *
     * @param p a pattern
     */
    void savePattern(Pattern p);

    void finish();

    void clear();

    /**
     * Print patterns
     *
     * @return a string
     */
    String print();

    /**
     * Save patterns to file
     *
     * @param patterns a list of patterns
     */
    void savePatterns(Collection<Pattern> patterns);


}