package algorithms.sequentialpatterns.prefixSpan_AGP.items.creators;

import algorithms.sequentialpatterns.prefixSpan_AGP.items.Item;
import algorithms.sequentialpatterns.prefixSpan_AGP.items.Pair;
import algorithms.sequentialpatterns.prefixSpan_AGP.items.PseudoSequence;
import algorithms.sequentialpatterns.prefixSpan_AGP.items.Sequence;
import algorithms.sequentialpatterns.prefixSpan_AGP.items.abstractions.Abstraction_Generic;
import algorithms.sequentialpatterns.prefixSpan_AGP.items.patterns.Pattern;

import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public abstract Abstraction_Generic CreateDefaultAbstraction();

    public abstract Map<Item, Set<Abstraction_Generic>> createAbstractions(Sequence sequence, Map<Item, BitSet> frequentItems);

    public abstract Set<Pair> findAllFrequentPairs(List<PseudoSequence> sequences);

    public abstract Abstraction_Generic createAbstractionFromAPrefix(Pattern prefix, Abstraction_Generic abstraction);
}
