package com.kalixia.xbee.api

import spock.lang.Specification

class XBeeSpec extends Specification {

    /*
    def "test ATID request"() {
        given:
        XBee xbee = null;

        when:
        def future = xbee.sendAtRequest("ID").waitUninterruptibly(10, TimeUnit.SECONDS)

        then:
        future != null
        future.response != null
        future.response != ""
    }
    */

    /*
    def "test sending request and waiting for acknowledgment"() {
        given: "an XBee and a request"
        XBee xbee = null
        XBeeRequest request = null

        when: "to send"
        def future = xbee.send(request)

        then: "we should not wait for more than 10 seconds before we've got an acknowledgment"
        future != null
        future.waitForAck(10, TimeUnit.SECONDS)
    }*/
}
