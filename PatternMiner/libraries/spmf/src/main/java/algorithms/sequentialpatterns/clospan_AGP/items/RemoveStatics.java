package algorithms.sequentialpatterns.clospan_AGP.items;

import algorithms.sequentialpatterns.clospan_AGP.items.abstractions.Abstraction_Qualitative;
import algorithms.sequentialpatterns.clospan_AGP.items.creators.ItemAbstractionPairCreator;
import algorithms.sequentialpatterns.clospan_AGP.items.patterns.PatternCreator;


/**
 * @author antonio
 */
public class RemoveStatics {

    public static void clear() {
        ItemAbstractionPairCreator.sclear();
        Abstraction_Qualitative.clear();
        PatternCreator.sclear();
    }
}
