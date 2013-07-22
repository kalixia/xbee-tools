package com.kalixia.xbee.api.xbee;

import com.kalixia.xbee.handler.codec.xbee.XBeeChannelInitializer;
import com.kalixia.xbee.utils.XBeeFrameIdGenerator;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxChannelOption;
import io.netty.channel.rxtx.RxtxDeviceAddress;
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
            channel.closeFuture().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    LOGGER.info("Stopping XBee channel pipeline...");
                    stopNetty();
                }
            });
        } finally {
            rxtxBootstrap.group().shutdownGracefully();
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
//        LOGGER.info("Waiting for 30 seconds for the XBee channel pipeline to stop...");
//        channel.closeFuture().await(30, TimeUnit.SECONDS);
//        rxtxBootstrap.shutdown();
        channel.closeFuture().sync();
    }

    @Override
    public XBeeModemInfo getModemInfo() {
        try {
            LOGGER.debug("Trying to write ATID command in channel {}", channel);
            ChannelFuture future = channel.write(new XBeeAtCommand(XBeeFrameIdGenerator.nextFrameID(), "ID"));
            future.await(TIMEOUT);
            LOGGER.debug("Completed? {}", future.isDone());
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
//                .addListener(new ChannelFutureListener() {
//                    @Override
//                    public void operationComplete(ChannelFuture future) throws Exception {
//                        LOGGER.info("Waiting for result of ATID!");
//                    }
//                });
        return new XBeeModemInfo((short) 0x1234);
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
