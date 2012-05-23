package com.kalixia.xbee.utils;

import java.util.Arrays;

public class HexUtils {

    public static String toHexString(int[] array) {
        switch (array.length) {
            case 2: // 16 bits
                return Integer.toHexString((array[0] << 8 & 0xffff) + array[1]);
            case 4:
                return toHexString(Arrays.copyOfRange(array, 0, 2)) + toHexString(Arrays.copyOfRange(array, 2, 4));
            case 8: // 64 bits
                return toHexString(Arrays.copyOfRange(array, 0, 4)) + toHexString(Arrays.copyOfRange(array, 4, 8));
        }
        return "";
    }

    public static String toHexStringPrefixed(int[]... array) {
        StringBuilder builder = new StringBuilder("0x");
        for (int[] sub : array) {
            builder.append(toHexString(sub));
        }
        return builder.toString();
    }

}
