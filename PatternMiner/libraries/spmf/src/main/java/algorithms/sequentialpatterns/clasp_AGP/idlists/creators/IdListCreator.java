package algorithms.sequentialpatterns.clasp_AGP.idlists.creators;

import algorithms.sequentialpatterns.clasp_AGP.dataStructures.Item;
import algorithms.sequentialpatterns.clasp_AGP.idlists.IDList;
import algorithms.sequentialpatterns.clasp_AGP.idlists.Position;
import algorithms.sequentialpatterns.clasp_AGP.tries.TrieNode;

import java.util.List;
import java.util.Map;

/**
 * Interface for a IdList Creator. If we are interested in adding any other kind
 * of IdList, we will have to define a creator which implements these three methods.
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
public interface IdListCreator {

    /**
     * It creates an empty IdList.
     *
     * @return the idlist
     */
    IDList create();

    /**
     * Add an appearance <sid,tid> to an Idlist.
     */
    void addAppearance(IDList idlist, Integer sequence, Integer timestamp, Integer item);

    /**
     * Add several appearances in a same sequence <sid, {tid_1,tid_2,...,tid_n}> to an Idlist
     */
    void addAppearancesInSequence(IDList idlist, Integer sequence, List<Position> itemsets);

    void initializeMaps(Map<Item, TrieNode> frequentItems, Map<Item, Map<Integer, List<Integer>>> projectingDistanceMap, Map<Integer, Integer> sequenceSize, Map<Integer, List<Integer>> itemsetSequenceSize);

    void updateProjectionDistance(Map<Item, Map<Integer, List<Integer>>> projectingDistanceMap, Item item, int id, int size, int i);
}
