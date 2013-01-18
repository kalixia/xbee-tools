package com.kalixia.xbee.handler.codec.xbee;

import com.kalixia.xbee.api.xbee.*;
import com.kalixia.xbee.api.zigbee.ZigBeeReceive;import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decoder which creates the appropriate API object.
 * Expect the input to be a {@link XBeePacket}, hence is usually preceded with the {@link XBeeFrameDelimiterDecoder}.
 * <p>
 * <strong>This decoder only work with AP = 1 yet</strong>
 */
public class XBeePacketDecoder extends MessageToMessageDecoder<XBeePacket> {
    private final int apVersion;
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeePacketDecoder.class);

    public XBeePacketDecoder(int apVersion) {
        super(XBeePacket.class);
        if (apVersion != 1)
            throw new IllegalArgumentException("The only API mode supported is the AP 1 yet");
        this.apVersion = apVersion;
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, XBeePacket packet) throws Exception {
        ByteBuf data = Unpooled.wrappedBuffer(packet.getData());

        switch (packet.getApiIdentifier()) {
            case MODEM_STATUS: {
                byte status = data.readByte();
                return XBeeModemStatus.valueOf(status);
            }
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
            case ZB_RX_PACKET: {
                byte frameID = data.readByte();
                XBeeAddress64 source64 = new XBeeAddress64(data.readLong());
                XBeeAddress16 source16 = new XBeeAddress16(data.readShort());
                XBeeReceive.Options options = new XBeeReceive.Options(data.readByte());
                byte appData[] = new byte[data.readableBytes()];
                data.readBytes(appData);
                return new ZigBeeReceive(frameID, source64, source16, options, appData);
            }
            case TX_STATUS: {
                byte frameID = data.readByte();
                byte status = data.readByte();
                return new XBeeTransmitStatus(frameID, status);
            }
            case AT_COMMAND_RESPONSE: {
                byte frameID = data.readByte();
                String command = new String(new byte[] { data.readByte(), data.readByte() });
                XBeeAtCommandResponse.Status status = XBeeAtCommandResponse.Status.fromValue(data.readByte());
                byte valueData[] = new byte[data.readableBytes()];
                data.readBytes(valueData);
                return new XBeeAtCommandResponse(frameID, command, status, valueData);
            }
            default:
                LOGGER.error(String.format("Unknown API identifier 0x%x", packet.getApiIdentifier()));
                return null;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
