package com.kalixia.xbee.handler.codec.xbee;

import com.kalixia.xbee.api.xbee.XBeeRequest;
import com.kalixia.xbee.api.xbee.XBeeTransmit;
import com.kalixia.xbee.api.xbee.XBeeTransmit16;
import com.kalixia.xbee.api.xbee.XBeeTransmit64;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encoder which creates the appropriate {@link XBeePacket}.
 * Expect the input to be an {@link XBeeRequest} object from the API.
 *
 * This encoder only work with AP = 1 yet.
 */
public class XBeePacketEncoder extends MessageToMessageEncoder<XBeeRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeePacketEncoder.class);

    @Override
    protected Object encode(ChannelHandlerContext ctx, XBeeRequest request) throws Exception {
        XBeePacket packet;
        if (request instanceof XBeeTransmit) {
            XBeeTransmit transmitRequest = (XBeeTransmit) request;
            byte[] data = transmitRequest.getData();
            // length is made of length(API identifier) + length(frameID)
            //                 + length(destination) + length(options) + length(data)
            int length = 1 + 1 + transmitRequest.getDestination().getLength() + 1 + data.length;
            if (request instanceof XBeeTransmit16)
                packet = new XBeePacket(XBeeApiIdentifier.TX_PACKET_16, length, data);
            else if (request instanceof XBeeTransmit64)
                packet = new XBeePacket(XBeeApiIdentifier.TX_PACKET_64, length, data);
            else
                throw new IllegalArgumentException("Unknown XBeeTransmit");
        } else {
            throw new IllegalArgumentException("Unknown XBee request");
        }
        LOGGER.debug("Generated packet {}", packet);
        return packet;
    }
}
