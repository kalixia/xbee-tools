package com.kalixia.xbee.handler.codec.xbee;

import com.kalixia.xbee.api.RSSI;
import com.kalixia.xbee.api.XBeeAddress16;
import com.kalixia.xbee.api.XBeeAddress64;
import com.kalixia.xbee.api.XBeeReceive;
import com.kalixia.xbee.api.XBeeReceive16;
import com.kalixia.xbee.api.XBeeReceive64;
import com.kalixia.xbee.api.XBeeTransmitStatus;
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
            case RX_PACKET_16: {
                XBeeAddress16 source = new XBeeAddress16(data.readShort());
                RSSI rssi = new RSSI(data.readByte());
                XBeeReceive.Options options = new XBeeReceive.Options(data.readByte());
                byte appData[] = new byte[data.readableBytes()];
                data.readBytes(appData);
                return new XBeeReceive16(source, rssi, options, appData);
            }
            case RX_PACKET_64: {
                XBeeAddress64 source = new XBeeAddress64(data.readLong());
                RSSI rssi = new RSSI(data.readByte());
                XBeeReceive.Options options = new XBeeReceive.Options(data.readByte());
                byte appData[] = new byte[data.readableBytes()];
                data.readBytes(appData);
                return new XBeeReceive64(source, rssi, options, appData);
            }
            case TX_STATUS: {
                byte frameID = data.readByte();
                byte status = data.readByte();
                return new XBeeTransmitStatus(frameID, status);
            }
            default:
                throw new IllegalStateException("Invalid API identifier " + packet.getApiIdentifier());
        }
    }
}
