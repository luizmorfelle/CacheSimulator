package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Scanner;

import static org.example.CacheSimulator.*;

public class Main {

    public static void main(String[] args) {
        // Parse command line arguments.
        int cacheSize = Integer.parseInt(args[0]);
        int associativity = Integer.parseInt(args[1]);
        replacementPolicy = Integer.parseInt(args[2]);
        writeBackPolicy = Integer.parseInt(args[3]);

        Cache cache = new Cache(associativity, cacheSize);
        Scanner scanner;
        scanner = new Scanner(Main.class.getClassLoader().getResourceAsStream("data.txt"));

        BigInteger address;
        BigInteger tag;
        int setNumber;
        String line;

        while (scanner.hasNextLine()) {
            String[] numbers = scanner.nextLine().split(" ");

            for (String number : numbers) {
                BigInteger bigInteger = new BigInteger(number);
                tag = bigInteger.divide(BigInteger.valueOf(BLOCK_SIZE)); // tag = address / BLOCK_SIZE
                setNumber = (tag.mod(BigInteger.valueOf(cache.numSets))).intValue(); // setNumber = tag % numSet
                totalRequests++;
                cache.write(tag, setNumber);
//                cache.read(tag, setNumber);
            }

            // Calculate new values.


            // Check the operation.
//            switch (operation) {
//                case 'R':
//                    cache.read(tag, setNumber);
//                    break;
//
//                case 'W':
//                    cache.write(tag, setNumber);
//                    break;
//
//                default:
//                    System.out.println("Error! Invalid operation.");
//            }

        }

        // Print the results.

        System.out.println("numMisses " + numMisses);
        System.out.println("totalRequests " + totalRequests);
        System.out.println("numWrites " + numWrites);
        System.out.println("numReads " + numReads);
    }
}