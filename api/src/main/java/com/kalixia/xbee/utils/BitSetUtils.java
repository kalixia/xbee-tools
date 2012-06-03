package com.kalixia.xbee.utils;

import java.util.BitSet;

public class BitSetUtils {
    public static BitSet toBitSet(byte flags) {
        // convert the byte into a @{link BitSet}
        BitSet bs = new BitSet(8);
        for (int i = 0; i < 8; i++) {
            boolean flag = (flags & (1 << i)) != 0;
            bs.set(i, flag);
        }
        return bs;
    }

    public static byte[] fromBitSet(BitSet bits) {
        byte[] b = new byte[1];
        for (int i = 0; i < bits.length(); i++) {
            if (bits.get(i) == true)
                b[0] |= 1 << i;
        }
        return b;
    }
}
