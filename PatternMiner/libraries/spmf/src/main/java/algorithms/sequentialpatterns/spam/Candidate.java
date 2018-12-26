package algorithms.sequentialpatterns.spam;

import java.util.Collection;

/**
 * This class implements a candidate as used by the TKS algorithm
 * for top-k sequential pattern mining. <br/><br/>
 * <p>
 * Copyright (c) 2013 Philippe Fournier-Viger
 * <br/><br/>
 * <p>
 * This file is part of the SPMF DATA MINING SOFTWARE
 * (http://www.philippe-fournier-viger.com/spmf).
 * <br/><br/>
 * <p>
 * SPMF is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <br/><br/>
 * <p>
 * SPMF is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <br/><br/>
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * SPMF. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoTKS
 * @see Prefix
 */
public class Candidate implements Comparable<Candidate> {

    Prefix prefix;
    Bitmap bitmap;
    Collection<Integer> sn;
    Collection<Integer> in;
    Integer hasToBeGreaterThanForIStep;
    int candidateLength = 0;

    public Candidate(Prefix prefix, Bitmap bitmap, Collection<Integer> sn,
                     Collection<Integer> in, Integer hasToBeGreaterThanForIStep, int candidateLength) {
        this.prefix = prefix;
        this.bitmap = bitmap;
        this.sn = sn;
        this.in = in;
        this.hasToBeGreaterThanForIStep = hasToBeGreaterThanForIStep;
        this.candidateLength = candidateLength;
    }

    public int compareTo(Candidate o) {
        if (o == this) {
            return 0;
        }
        int compare = o.bitmap.getSupport() - this.bitmap.getSupport();
        if (compare != 0) {
            return compare;
        }
        compare = this.hashCode() - o.hashCode();
        if (compare != 0) {
            return compare;
        }
        compare = prefix.size() - o.prefix.size();
        if (compare != 0) {
            return compare;
        }
        return hasToBeGreaterThanForIStep - o.hasToBeGreaterThanForIStep;
    }

}
