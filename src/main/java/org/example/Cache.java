package org.example;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.example.CacheSimulator.*;

public class Cache {
    int associativity;
    int numSets;
    int size;
    Block[][] blocks;
    ArrayList<LinkedList<Integer>> metadata;

    Cache(int associativity, int cacheSize) {
        this.associativity = associativity;
        size = cacheSize;
        numSets = cacheSize / (BLOCK_SIZE * associativity);
        blocks = new Block[numSets][associativity];
        metadata = new ArrayList<>();

        // Initialize blocks.
        for (int i = 0; i < blocks.length; i++)
            for (int j = 0; j < blocks[i].length; j++)
                blocks[i][j] = new Block();

        // Initialize metadata.
        for (int i = 0; i < numSets; i++)
            metadata.add(new LinkedList<Integer>());
    }

    // Finds a free cache block to be used.
    int getFreeBlock(int setNumber) {
        LinkedList<Integer> set = metadata.get(setNumber);

        // Check if there is a free block.
        for (int i = 0; i < associativity; i++)
            if (blocks[setNumber][i].isEmpty)
                return i;

        return set.remove();
    }

    // Returns the index for the given tag. Returns -1 if the tag is not in the cache.
    int indexOf(BigInteger tag, int setNumber) {
        for (int i = 0; i < associativity; i++)
            if (blocks[setNumber][i].tag != null && blocks[setNumber][i].tag.compareTo(tag) == 0)
                return i;

        return -1;
    }

    // Reads a tag from the cache in the specified set.
    void read(BigInteger tag, int setNumber) {
        int index = indexOf(tag, setNumber);

        // Check for a hit.
        if (index != -1) {
            updateMetadata(setNumber, index);
        }

        // Check for a miss.
        else {
            numMisses++;
            index = getFreeBlock(setNumber);
            Block block = blocks[setNumber][index];

            block.tag = tag;
            block.isEmpty = false;
            numReads++;

            if (writeBackPolicy == WRITE_BACK) {
                if (block.isDirty)
                    numWrites++;

                block.isDirty = false;
            }

            updateMetadata(setNumber, index);
        }
    }

    // Updates the cache metadata according to the specified replacement policy.
    void updateMetadata(int setNumber, int index) {
        LinkedList<Integer> set = metadata.get(setNumber);

        // Check if the queue is empty.
        if (set.size() == 0) {
            set.add(index);
        } else {
            if (replacementPolicy == LRU) {
                int targetIndex = set.indexOf(index);

                if (targetIndex != -1)
                    set.remove(targetIndex);
            }

            set.add(index);
        }
    }

    // Writes a tag to the cache in the specified set.
    void write(BigInteger tag, int setNumber) {
        Block block;
        int index = indexOf(tag, setNumber);

        // Check for a hit.
        if (index != -1) {
            block = blocks[setNumber][index];

            block.tag = tag;
            block.isEmpty = false;

            // Check the replacement policy.
            switch (writeBackPolicy) {
                case WRITE_THROUGH:
                    numWrites++;
                    break;

                case WRITE_BACK:
                    block.isDirty = true;
                    break;
            }

            updateMetadata(setNumber, index);
        }

        // Check for a miss.
        else {
            numMisses++;
            index = getFreeBlock(setNumber);
            block = blocks[setNumber][index];
            block.tag = tag;
            block.isEmpty = false;
            numReads++;

            // Check the replacement policy.
            switch (writeBackPolicy) {
                case WRITE_THROUGH:
                    numWrites++;
                    break;

                case WRITE_BACK:
                    if (block.isDirty)
                        numWrites++;
                    blocks[setNumber][index].isDirty = true;
                    break;
            }

            updateMetadata(setNumber, index);
        }
    }
}

