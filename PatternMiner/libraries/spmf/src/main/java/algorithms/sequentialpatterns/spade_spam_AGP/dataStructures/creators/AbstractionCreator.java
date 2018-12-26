package algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.creators;

import algorithms.sequentialpatterns.spade_spam_AGP.EquivalenceClass;
import algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.Item;
import algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.Sequence;
import algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.abstractions.Abstraction_Generic;
import algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.patterns.Pattern;
import algorithms.sequentialpatterns.spade_spam_AGP.idLists.creators.IdListCreator;

import java.util.List;
import java.util.Map;

/**
 * Abstract class that is thought to make it possible the creation of any kind
 * of abstractions.
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
public abstract class AbstractionCreator {

    public abstract Abstraction_Generic createDefaultAbstraction();

    public abstract List<EquivalenceClass> getFrequentSize2Sequences(List<Sequence> sequences, IdListCreator idListCreator);

    public abstract Pattern getSubpattern(Pattern extension, int i);

    public abstract List<EquivalenceClass> getFrequentSize2Sequences(Map<Integer, Map<Item, List<Integer>>> database, Map<Item, EquivalenceClass> frequentItems, IdListCreator idListCreator);

    public abstract void clear();
}
