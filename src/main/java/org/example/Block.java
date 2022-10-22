package org.example;

import java.math.BigInteger;

public class Block {
    BigInteger tag;
    boolean isDirty;
    boolean isEmpty;

    Block()
    {
        tag = BigInteger.valueOf(0);
        isDirty = false;
        isEmpty = true;
    }

}
