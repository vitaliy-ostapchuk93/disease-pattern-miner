/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.sequentialpatterns.gsp_AGP.items;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of a class that maintain a pool with all the different
 * items that have been generated.
 * <p>
 * Copyright Antonio Gomariz Peñalver 2013
 * <p>
 * This file is part of the SPMF DATA MINING SOFTWARE
 * (http://www.philippe-fournier-viger.com/spmf).
 * <p>
 * SPMF is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * SPMF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with SPMF.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @author agomariz
 */

public class ItemFactory<T extends Comparable> {

    /**
     * The pool where all the items are kept
     */
    private Map<T, Item> pool = new HashMap<T, Item>();

    /**
     * The only constructor
     */
    public ItemFactory() {
    }

    /**
     * Method to obtain the reference of an item if this has already been
     * created
     *
     * @param key the key of the item
     * @return the item
     */
    public Item getItem(T key) {
        Item item = pool.get(key);
        if (item == null) {
            item = new Item(key);
            pool.put(key, item);
        }
        return item;
    }
}
