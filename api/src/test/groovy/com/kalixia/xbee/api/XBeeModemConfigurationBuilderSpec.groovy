package com.kalixia.xbee.api

import spock.lang.Specification
import com.kalixia.xbee.api.xbee.XBeeAddress16
import com.kalixia.xbee.api.xbee.XBeeModemConfigurationBuilder
import com.kalixia.xbee.api.xbee.XBeeModemConfiguration

class XBeeModemConfigurationBuilderSpec extends Specification {
    def "simple XBee Modem configuration"() {
        given: "a XBee Modem configuration builder"
        XBeeModemConfigurationBuilder builder = new XBeeModemConfigurationBuilder()

        when: "building the configuration"
        XBeeModemConfiguration modemConfiguration = builder
                .withPan(1234)
                .withChannel(0x0C)
                .withAddress(new XBeeAddress16(1))
                .withNodeIdentifier("My Node")
                .withDestination(new XBeeAddress16(2))
                .build()

        then: "expect the configuration to be set properly"
        modemConfiguration != null
        modemConfiguration.panID == 1234
        modemConfiguration.channel == 0x0C
        modemConfiguration.address16.value == 1
        modemConfiguration.nodeIdentifier.equals "My Node"
        modemConfiguration.destination.equals(new XBeeAddress16(2))
    }

    /*
            // write changes and ensure the configuration has been properly applied
            builder.writeChanges();
            XBeeModemConfiguration currentConfiguration = builder.getCurrentConfiguration();
            assert modemConfiguration.equals(currentConfiguration);
     */
}
