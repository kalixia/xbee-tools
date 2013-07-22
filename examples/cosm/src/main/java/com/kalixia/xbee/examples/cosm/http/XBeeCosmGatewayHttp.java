package com.kalixia.xbee.examples.cosm.http;

import com.kalixia.xbee.examples.cosm.XBeeCosmGateway;
import com.kalixia.xbee.examples.cosm.XBeeRequestToCosmHandler;
import com.kalixia.xbee.handler.codec.xbee.XBeeChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxChannelOption;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.URISyntaxException;

public class XBeeCosmGatewayHttp implements XBeeCosmGateway {
    private String serialPort;
    private Integer baudRate;

    private String apiKey;
    private Long feedID;
    private Integer datastreamID;

    private CosmClientInboundHandler cosmClientInboundHandler;
    private Bootstrap cosmBootstrap, rxtxBootstrap;

    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeCosmGatewayHttp.class);

    @Override
    public void start(String serialPort, Integer baudRate, String apiKey, Long feedID, Integer datastreamID)
            throws InterruptedException, URISyntaxException {
        this.serialPort = serialPort;
        this.baudRate = baudRate;
        this.apiKey = apiKey;
        this.feedID = feedID;
        this.datastreamID = datastreamID;

        LOGGER.debug("Should start HTTP API...");
        try {
            // configure Cosm client
            Channel cosmChannel = bootstrapCosm();

            // configure the RXTX client
            bootstrapRxtx(cosmChannel).closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rxtxBootstrap.group().shutdownGracefully();
            cosmBootstrap.group().shutdownGracefully();
        }
    }

    private Channel bootstrapCosm() throws InterruptedException, URISyntaxException {
        cosmClientInboundHandler = new CosmClientInboundHandler();

        cosmBootstrap = new Bootstrap();
        cosmBootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress("api.cosm.com", 80))
                .handler(new CosmChannelInitializer());
        LOGGER.info("COSM client connecting...");
        return cosmBootstrap.connect().sync().channel();
    }

    private Channel bootstrapRxtx(Channel cosmChannel) throws InterruptedException {
        rxtxBootstrap = new Bootstrap();
        rxtxBootstrap.group(new OioEventLoopGroup())
                .channel(RxtxChannel.class)
                .remoteAddress(new RxtxDeviceAddress(serialPort))
                .option(RxtxChannelOption.BAUD_RATE, baudRate)
                .handler(new RxtxChannelInitializer(cosmChannel));
        LOGGER.info("Listening for serial data on {} at {} bauds...", serialPort, baudRate);
        return rxtxBootstrap.connect().sync().channel();
    }

    public class CosmChannelInitializer extends ChannelInitializer<NioSocketChannel> {
        @Override
        public void initChannel(NioSocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast("logging", new LoggingHandler());
            pipeline.addLast("http-codec", new HttpClientCodec());
            pipeline.addLast("cosm-client-encoder", new XBeeCosmClientMessageEncoder());
            pipeline.addLast("cosm-client-inbound-handler", cosmClientInboundHandler);
        }
    }

    public class RxtxChannelInitializer extends XBeeChannelInitializer {
        private final Channel cosmChannel;

        public RxtxChannelInitializer(Channel cosmChannel) {
            this.cosmChannel = cosmChannel;
        }

        @Override
        public void initChannel(RxtxChannel ch) throws Exception {
            super.initChannel(ch);
            ch.pipeline().addLast("xbee-request-to-cosm",
                    new XBeeRequestToCosmHandler(cosmChannel, apiKey, feedID, datastreamID));
        }

    }
}
