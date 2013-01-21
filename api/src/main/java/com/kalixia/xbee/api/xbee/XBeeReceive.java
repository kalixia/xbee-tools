package com.kalixia.xbee.api.xbee;

import com.kalixia.xbee.utils.BitSetUtils;

import java.io.Serializable;
import java.util.BitSet;

public interface XBeeReceive extends XBeeRequest {
    XBeeAddress getSource();
    RSSI getRssi();
    Options getOptions();

    /**
     * bit 0 [reserved]
     * bit 1 = Address broadcast
     * bit 2 = PAN broadcast
     * bits 3-7 [reserved]
     */
    public class Options implements Serializable {
        private final boolean addressBroadcast;
        private final boolean panBroadcast;

        public Options(byte flags) {
            BitSet bs = BitSetUtils.toBitSet(flags);
            addressBroadcast = bs.get(1);
            panBroadcast = bs.get(2);
        }

        public boolean isAddressBroadcast() {
            return addressBroadcast;
        }

        public boolean isPanBroadcast() {
            return panBroadcast;
        }

        @Override
        public String toString() {
            return "Options{" +
                    "addressBroadcast=" + addressBroadcast +
                    ", panBroadcast=" + panBroadcast +
                    '}';
        }
    }
}
