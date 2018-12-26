package algorithms.frequentpatterns.upgrowth_ihup;

import java.util.ArrayList;
import java.util.List;


/**
 * This is an implementation of the UP-Node structure used by Up Tree in
 * UPGrowthPlus algorithm.
 * <p>
 * Copyright (c) 2015 Prashant Barhate
 * <p>
 * This file is part of the SPMF DATA MINING SOFTWARE *
 * (http://www.philippe-fournier-viger.com/spmf).
 * <p>
 * SPMF is free software: you can redistribute it and/or modify it under the *
 * terms of the GNU General Public License as published by the Free Software *
 * Foundation, either version 3 of the License, or (at your option) any later *
 * version. SPMF is distributed in the hope that it will be useful, but WITHOUT
 * ANY * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * SPMF. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Prashant Barhate
 * @see AlgoUPGrowthPlus
 * @see UPTreePlus
 */

public class UPNodePlus {
    // String itemName;
    int itemID = -1;
    int count = 1;
    int nodeUtility;
    int minimalNodeUtility;
    UPNodePlus parent = null;
    // the child nodes of that node
    List<UPNodePlus> childs = new ArrayList<UPNodePlus>();

    UPNodePlus nodeLink = null; // link to next node with the same item id (for the
    // header table).

    /**
     * Default constructor
     */
    public UPNodePlus() {
    }

    /**
     * method to get child node Return the immediate child of this node having a
     * given ID(item itself). If there is no such child, return null;
     */
    UPNodePlus getChildWithID(int name) {
        // for each child node
        for (UPNodePlus child : childs) {
            // if the ID(item itself) is the one that we are looking for
            if (child.itemID == name) {
                // return that node
                return child;
            }
        }
        // if not found, return null
        return null;
    }

    @Override
    public String toString() {
        return "(i=" + itemID + " count=" + count + " nu=" + nodeUtility + ")";
    }

}