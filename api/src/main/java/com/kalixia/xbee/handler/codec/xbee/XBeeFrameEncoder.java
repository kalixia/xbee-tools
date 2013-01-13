package com.kalixia.xbee.handler.codec.xbee;

import com.kalixia.xbee.api.xbee.XBeeRequest;
import com.kalixia.xbee.api.xbee.XBeeTransmit16;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encoder which creates the appropriate XBee frame data to send to the XBee module.
 * Expect the input to be an {@link XBeeRequest} object from the API.
 *
 * This encoder only work with AP = 1 yet.
 */
public class XBeeFrameEncoder extends MessageToByteEncoder<XBeeRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeFrameEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, XBeeRequest request, ByteBuf out) throws Exception {
        ByteBuf raw = Unpooled.buffer();

        // length is made of API identifier + request data length
        ByteBuf packet = request.serialize();
        int length = 1 + packet.readableBytes();

        out.writeByte(XBeeFrameDelimiterDecoder.START_DELIMITER);
        out.writeShort(length);

        if (request instanceof XBeeTransmit16) {
            raw.writeByte(XBeeApiIdentifier.TX_PACKET_16.getApiIdentifier());
        } else {
            throw new UnsupportedOperationException(String.format("Can't encode %s packets",
                    request.getClass().getName()));
        }

        raw.writeBytes(packet);

        byte checksum = calculateChecksum(raw);

        out.writeBytes(raw);
        out.writeByte(checksum);
    }

    public byte calculateChecksum(ByteBuf data) {
        byte computed = 0;
        data.markReaderIndex();
        for (int i = 0; i < data.readableBytes() - 1; i++) {
            computed += data.readByte();
        }
        data.resetReaderIndex();
        return (byte) (0xFF - computed);
    }
}
