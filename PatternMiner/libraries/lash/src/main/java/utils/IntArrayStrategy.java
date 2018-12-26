package utils;

import it.unimi.dsi.fastutil.Hash.Strategy;

import java.util.Arrays;

//import gnu.trove.TObjectHashingStrategy;


public final class IntArrayStrategy implements Strategy<int[]> {


    @Override
    public boolean equals(int[] o1, int[] o2) {
        return Arrays.equals(o1, o2);
    }

    @Override
    public int hashCode(int[] o) {
        return Arrays.hashCode(o);
    }


}