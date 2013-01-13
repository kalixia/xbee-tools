package com.kalixia.xbee.handler.codec.xbee;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decoder which analyzes serial data input and generate appropriate frames as {@link XBeePacket}s.
 */
public class XBeeFrameDelimiterDecoder extends ByteToMessageDecoder {
    public static final byte START_DELIMITER = 0x7E;
    private static final int MAX_LENGTH = 110;
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeFrameDelimiterDecoder.class);

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in.readableBytes() < 1 + 2) {  // 1 byte for start delimiter + 2 bytes for packet length
            return null;
        }

        in.markReaderIndex();

        // ensure that we've got the API start delimiter
        byte start = in.readByte();

        if (start != START_DELIMITER) {
            LOGGER.error("Oops, received {} instead of {}. Skipping byte...",
                    Integer.toHexString(start), Integer.toHexString(START_DELIMITER));
//            throw new IllegalStateException("Invalid start delimiter");
            return null;
        }

        // read the packet length
        short length = in.readShort();
        if (length < 2) {
            LOGGER.error("XBee Packet is too short: {} bytes!", length);
            return null;
        }

        // make sure if there's enough bytes in the buffer.
        if (in.readableBytes() < length + 1) {     // data to read + 1 byte for the checksum
            // The whole bytes were not received yet - return null.
            // This method will be invoked again when more packets are received and appended to the buffer.

            // Reset to the marked position to read the length field again next time.
            in.resetReaderIndex();

            return null;
        }

        // figure out XBee API identifier
        XBeeApiIdentifier apiIdentifier = XBeeApiIdentifier.valueOf(in.readByte());

        // there's enough bytes in the buffer. Read it.
        ByteBuf frame = in.readBytes(length - 1);    // we have already read API identifier

        // read checksum
        byte checksum = in.readByte();

        XBeePacket packet = new XBeePacket(apiIdentifier, length, frame.array(), checksum);

        // log what we've got
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Frame size is {}. API Identifier is: {}. Checksum is: {}", new Object[]{
                    length,
                    packet.getApiIdentifier().name(),
                    "0x" + ByteBufUtil.hexDump(Unpooled.wrappedBuffer(new byte[]{packet.getChecksum()}))
            });
            LOGGER.debug("Frame content is: {}", packet.getData());
        }

        if (!packet.verifyChecksum())
            throw new IllegalStateException("Invalid checksum for message");


        // Successfully decoded a frame.  Return the decoded frame.
        return packet;
    }
}
