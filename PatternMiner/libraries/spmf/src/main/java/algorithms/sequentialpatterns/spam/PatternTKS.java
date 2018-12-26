package algorithms.sequentialpatterns.spam;

/**
 * Implementation of a pattern found by the TKS algorithm.
 * <br/><br/>
 * <p>
 * Copyright (c) 2013 Philippe Fournier-Viger, Antonio Gomariz
 * <br/><br/>
 * <p>
 * This file is part of the SPMF DATA MINING SOFTWARE
 * (http://www.philippe-fournier-viger.com/spmf).
 * <p>
 * <br/><br/>
 * SPMF is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <br/><br/>
 * <p>
 * SPMF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <br/><br/>
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with SPMF. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Philippe Fournier-Viger  & Antonio Gomariz
 * @see AlgoTKS
 * @see Prefix
 */
public class PatternTKS implements Comparable<PatternTKS> {

    /**
     * the support of the pattern
     */
    public int support;
    /**
     * the pattern
     */
    Prefix prefix;
    /**
     * the bitset corresponding to this pattern, which indicates the sequences containing this pattern (optional)
     */
    Bitmap bitmap;

    /**
     * the constructor
     */
    public PatternTKS(Prefix prefix, int suppport) {
        this.prefix = prefix;
        this.support = suppport;
    }

    public int compareTo(PatternTKS o) {
        if (o == this) {
            return 0;
        }
        int compare = this.support - o.support;
        if (compare != 0) {
            return compare;
        }

        return this.hashCode() - o.hashCode();
    }

}
