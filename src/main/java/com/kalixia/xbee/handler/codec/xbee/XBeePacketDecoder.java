package com.kalixia.xbee.handler.codec.xbee;

import com.kalixia.xbee.api.RSSI;
import com.kalixia.xbee.api.XBeeAddress16;
import com.kalixia.xbee.api.XBeeRequest;
import com.kalixia.xbee.api.XBeeRequest16;
import com.kalixia.xbee.api.XBeeRequest64;
import io.netty.buffer.ChannelBuffer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.oneone.OneToOneDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decoder which create the appropriate API object.
 * Expect the input to be a {@link XBeePacket}, hence is usually preceded with the {@link XBeeFrameDelimiterDecoder}.
 */
public class XBeePacketDecoder extends OneToOneDecoder {
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeePacketDecoder.class);

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        XBeePacket packet = (XBeePacket) msg;
        ChannelBuffer data = ((XBeePacket) msg).getData();

        switch (packet.getApiIdentifier()) {
            case RX_PACKET_16:
                XBeeAddress16 source = new XBeeAddress16(data.readByte() * 256 + data.readByte());
                RSSI rssi = new RSSI(data.readByte());
                XBeeRequest.Options options = new XBeeRequest.Options(data.readByte());
                byte appData[] = new byte[data.readableBytes()];
                data.readBytes(appData);
                return new XBeeRequest16(source, rssi, options, appData);
            case RX_PACKET_64:
                return new XBeeRequest64();
            default:
                throw new IllegalStateException("Invalid API identifier " + packet.getApiIdentifier());
        }
    }
}
