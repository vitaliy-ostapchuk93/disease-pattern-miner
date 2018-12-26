package algorithms.sequentialpatterns.clasp_AGP.dataStructures.creators;

import algorithms.sequentialpatterns.clasp_AGP.dataStructures.abstracciones.Abstraction_Generic;
import algorithms.sequentialpatterns.clasp_AGP.dataStructures.patterns.Pattern;

import java.util.List;

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

    public abstract Pattern getSubpattern(Pattern extension, int i);

    public abstract void clear();

    public abstract boolean isSubpattern(Pattern aThis, Pattern p, int i, List<Integer> posiciones);
}
