package algorithms.frequentpatterns.hui_miner;

/* This file is copyright (c) 2008-2013 Philippe Fournier-Viger
 *
 * This file is part of the SPMF DATA MINING SOFTWARE
 * (http://www.philippe-fournier-viger.com/spmf).
 *
 * SPMF is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SPMF is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with
 * SPMF. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * This class represents a UtilityList as used by the FCHM_all_confidence algorithm.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoHUIMiner
 * @see Element
 */
public class UtilityListFCHM_all_confidence extends UtilityList {

    /**
     * support of the maximum subset
     */
    public int max_subset;

    /**
     * Constructor
     *
     * @param item an item
     */
    public UtilityListFCHM_all_confidence(Integer item) {
        super(item);
        // TODO Auto-generated constructor stub
    }

    /**
     * Get the all-confidence
     *
     * @return the all confidence as a double value
     */
    public double getAll_confidence() {
        return elements.size() / (double) max_subset;
    }

}
