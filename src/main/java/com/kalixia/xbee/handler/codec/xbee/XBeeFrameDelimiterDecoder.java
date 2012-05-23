package com.kalixia.xbee.handler.codec.xbee;

import io.netty.buffer.ChannelBuffer;
import io.netty.buffer.ChannelBuffers;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.Charset;

/**
 * Decoder which analyzes serial data input and generate appropriate frames as {@link XBeePacket}s.
 */
public class XBeeFrameDelimiterDecoder extends FrameDecoder {
    private static final int START_DELIMITER = 0x7E;
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeFrameDelimiterDecoder.class);

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buf) throws Exception {
        if (buf.readableBytes() < 1 + 2) {
            return null;
        }

        buf.markReaderIndex();

        // ensure that we've got the API start delimiter
        byte start = buf.readByte();

        if (start != START_DELIMITER) {
            LOGGER.error("Oops, received {} instead of {}",
                    Integer.toHexString(start), Integer.toHexString(START_DELIMITER));
        }

        // read the packet length
        int length = buf.readByte() * 256 + buf.readByte();

        // Make sure if there's enough bytes in the buffer.
        if (buf.readableBytes() < length + 1) {     // data to read + 1 byte for the checksum
            // The whole bytes were not received yet - return null.
            // This method will be invoked again when more packets are
            // received and appended to the buffer.

            // Reset to the marked position to read the length field again
            // next time.
            buf.resetReaderIndex();

            return null;
        }

        XBeeApiIdentifier apiIdentifier = XBeeApiIdentifier.valueOf(buf.readByte());

        // There's enough bytes in the buffer. Read it.
        ChannelBuffer frame = buf.readBytes(length - 1);    // we have already read API identifier

        // Read checksum
        byte checksum = buf.readByte();

        XBeePacket packet = new XBeePacket(apiIdentifier, length, frame, checksum);

        // Log what we've got
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Frame size is {}. API Identifier is: {}. Checksum is: {}", new Object[]{
                    length,
                    packet.getApiIdentifier().name(),
                    "0x" + ChannelBuffers.hexDump(ChannelBuffers.wrappedBuffer(new byte[]{packet.getChecksum()}))
            });
//            LOGGER.debug("Frame content is: {}", frame.toString(Charset.defaultCharset()));
        }

        if (!packet.validateChecksum())
            throw new IllegalStateException("Invalid checksum for message");


        // Successfully decoded a frame.  Return the decoded frame.
        return packet;
    }
}
