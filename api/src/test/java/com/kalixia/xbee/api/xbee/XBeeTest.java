package com.kalixia.xbee.api.xbee;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XBeeTest {

//    @Test
//    public void testLifecycle() throws InterruptedException {
//        XBee xbee = XBeeFactory.newInstance();
//        xbee.begin("/dev/tty.usbserial-A100MZ0L", 57600);
//        xbee.stop();
//    }

    @Test
    public void testModemInfo() throws InterruptedException {
        XBee xbee = XBeeFactory.newInstance();
        xbee.begin("/dev/tty.usbserial-A100MZ0L", 57600);
        XBeeModemInfo modemInfo = xbee.getModemInfo();
        assertNotNull(modemInfo);
        assertEquals(0x1234, modemInfo.getPanID());
//        xbee.stop();
    }

    public static void main(String[] args) throws InterruptedException {
        new XBeeTest().testModemInfo();
    }

}
