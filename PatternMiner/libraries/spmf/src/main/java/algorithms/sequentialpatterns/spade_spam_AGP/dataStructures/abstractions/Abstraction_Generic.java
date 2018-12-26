package algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.abstractions;

/**
 * Abstract Class to enable any kind of abstraction.
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
public abstract class Abstraction_Generic implements Comparable<Abstraction_Generic> {

    @Override
    public abstract boolean equals(Object arg);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();

    public abstract String toStringToFile();
}
