package com.kalixia.xbee.api.xbee;

import com.kalixia.xbee.handler.codec.xbee.XBeeChannelInitializer;
import com.kalixia.xbee.utils.XBeeFrameIdGenerator;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxChannelOption;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import io.netty.channel.socket.oio.OioEventLoopGroup;
import io.netty.logging.InternalLoggerFactory;
import io.netty.logging.Slf4JLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the XBee interface.
 */
class XBeeImpl implements XBee {
    private boolean runnning = false;
    private Bootstrap rxtxBootstrap;
    private Channel channel;
    private static final long TIMEOUT = 5 * 1000;        // 5 seconds
    private static final Logger LOGGER = LoggerFactory.getLogger(XBee.class);

    static {
        // Configure Netty logging
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
    }

    @Override
    public XBee begin(String serialPort, int bauds) throws InterruptedException {
        // start Netty channel
        startNetty(serialPort, bauds);
        runnning = true;
        return this;
    }

    private void startNetty(String serialPort, int bauds) throws InterruptedException {
        rxtxBootstrap = new Bootstrap();
        try {
            rxtxBootstrap.group(new OioEventLoopGroup())
                    .channel(RxtxChannel.class)
                    .remoteAddress(new RxtxDeviceAddress(serialPort))
                    .option(RxtxChannelOption.BAUD_RATE, bauds)
                    .handler(new XBeeChannelInitializer());
            LOGGER.info("Listening for serial data on {} at {} bauds...", serialPort, bauds);
            ChannelFuture f = rxtxBootstrap.connect().sync();

            channel = f.channel();
//            channel.closeFuture().addListener(new ChannelFutureListener() {
//                @Override
//                public void operationComplete(ChannelFuture future) throws Exception {
//                    LOGGER.info("Stopping XBee channel pipeline...");
//                    stopNetty();
//                }
//            });
        } finally {
            rxtxBootstrap.shutdown();
        }
    }

    @Override
    public XBee stop() throws InterruptedException {
        // stop Netty channel
        stopNetty();
        runnning = false;
        return this;
    }

    private void stopNetty() throws InterruptedException {
        rxtxBootstrap.shutdown();
        channel.closeFuture().sync();
    }

    @Override
    public XBeeModemInfo getModemInfo() throws InterruptedException {
        final XBeeModemInfo modemInfo = new XBeeModemInfo();
        channel.pipeline().addLast("modem-info", new ChannelInboundMessageHandlerAdapter<XBeeAtCommandResponse>() {
            @Override
            protected void messageReceived(ChannelHandlerContext ctx, XBeeAtCommandResponse msg) throws Exception {
                byte[] data = msg.getData();
                if ("ID".equals(msg.getCommand())) {
                    short panID = (short) ((data[0] & 0xFF) * 256 + data[1]);
                    LOGGER.debug("Found PAN ID: 0x{}", Integer.toHexString(panID));
                    modemInfo.setPanID(panID);
                } else if ("CH".equals(msg.getCommand())) {
                    modemInfo.setChannel(data[0]);
                } else if ("MY".equals(msg.getCommand())) {
                    short localAddress = (short) ((data[0] & 0xFF) * 256 + data[1]);
                    XBeeAddress16 address = new XBeeAddress16(localAddress);
                    modemInfo.setLocalAddress(address);
                    LOGGER.debug("Local XBee Address is {}", address);
                } else if ("NI".equals(msg.getCommand())) {
                    String name = new String(data);
                    LOGGER.debug("Node name is {}", name);
                    modemInfo.setName(name);
                } else {
                    LOGGER.error("Received unexpected XBee message: {}", msg);
                }
            }
        });

        // TODO: go through {@link #sendAtRequest} instead!
        channel.write(new XBeeAtCommand(XBeeFrameIdGenerator.nextFrameID(), "ID"));
        channel.write(new XBeeAtCommand(XBeeFrameIdGenerator.nextFrameID(), "CH"));
        channel.write(new XBeeAtCommand(XBeeFrameIdGenerator.nextFrameID(), "MY"));
        channel.write(new XBeeAtCommand(XBeeFrameIdGenerator.nextFrameID(), "NI"));

        while (channel.isActive())
            Thread.sleep(100);
        channel.pipeline().remove("modem-info");

//        LOGGER.info("Modem info is {}", modemInfo);
        return modemInfo;
    }

    @Override
    public XBeeModemStatus getModemStatus() {
        throw new UnsupportedOperationException();  // TODO: write this!
    }

    @Override
    public XBee withModemConfiguration(XBeeModemConfiguration configuration) {
        throw new UnsupportedOperationException();  // TODO: write this!
    }

    @Override
    public XBeeFuture send(XBeeRequest request) {
        throw new UnsupportedOperationException();  // TODO: write this!
    }

    @Override
    public XBeeFuture sendAtRequest(String request) {
        throw new UnsupportedOperationException();  // TODO: write this!
    }

    @Override
    public XBee addListener(XBeeListener listener) {
        throw new UnsupportedOperationException();  // TODO: write this!
    }
}
