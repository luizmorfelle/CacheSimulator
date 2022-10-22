package org.example;

public class CacheSimulator {
    public static final int BLOCK_SIZE = 64;
    public static final int LRU = 0;
    public static final int WRITE_THROUGH = 0;
    public static final int WRITE_BACK = 1;


    static int replacementPolicy;
    static int writeBackPolicy;

    static double numMisses = 0;
    static double numReads = 0;
    static double numWrites = 0;
    static double totalRequests = 0;
}
