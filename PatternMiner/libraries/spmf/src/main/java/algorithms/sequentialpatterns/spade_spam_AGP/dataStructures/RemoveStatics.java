package algorithms.sequentialpatterns.spade_spam_AGP.dataStructures;

import algorithms.sequentialpatterns.spade_spam_AGP.candidatePatternsGeneration.CandidateGenerator_Qualitative;
import algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.abstractions.Abstraction_Qualitative;
import algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.creators.AbstractionCreator_Qualitative;
import algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.creators.ItemAbstractionPairCreator;
import algorithms.sequentialpatterns.spade_spam_AGP.idLists.creators.IdListCreator_StandardMap;

/**
 * Class that remove the remaining static fields.
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
public class RemoveStatics {

    public static void clear() {
        ItemAbstractionPairCreator.sclear();
        Abstraction_Qualitative.clear();
        AbstractionCreator_Qualitative.sclear();
        IdListCreator_StandardMap.clear();
        CandidateGenerator_Qualitative.clear();

    }
}
