package com.kalixia.xbee.examples.cosm;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.kalixia.xbee.handler.codec.xbee.XBeeChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxChannelOption;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import io.netty.channel.socket.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.oio.OioEventLoopGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocket13FrameEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.logging.InternalLoggerFactory;
import io.netty.logging.Slf4JLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class XBeeCosm {
    @Parameter(description = "The serial port to use for communication with the XBee module")
    public List<String> serialPorts = new ArrayList<String>();

    @Parameter(names = {"-b", "--baud"}, description = "Baud rate")
    private Integer baudRate = 9600;

    @Parameter(names = {"-f", "--feed"}, description = "COSM/Pachube feed ID to which the data should be sent")
    private Long feedID;

    @Parameter(names = {"-d", "--data-stream"}, description = "Data stream to update on the feed")
    private Integer datastreamID;

    @Parameter(names = {"-api"}, description = "API Key for COSM/Pachube", required = true)
    private String apiKey;

    private CosmClientInboundHandler cosmClientInboundHandler;
    private Bootstrap cosmBootstrap, rxtxBootstrap;

    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeCosm.class);

    public void start() throws Exception {
        // configure Netty logging
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());

        // show debug info
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("About to connect to COSM with API key {}", apiKey);
        }

        try {
            // configure Cosm client
            Channel cosmChannel = bootstrapCosm();

            // configure the RXTX client
            bootstrapRxtx(cosmChannel).closeFuture().sync();
        } finally {
            rxtxBootstrap.shutdown();
            cosmBootstrap.shutdown();
        }
    }

    private Channel bootstrapCosm() throws InterruptedException, URISyntaxException {
        URI uri = new URI("ws://api.cosm.com:8080");

        // COSM headers expected
        HttpHeaders customHeaders = new DefaultHttpHeaders();
        customHeaders.add("MyHeader", "MyValue");

        cosmClientInboundHandler = new CosmClientInboundHandler(
                WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, false, customHeaders));

        cosmBootstrap = new Bootstrap();
        cosmBootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .remoteAddress(uri.getHost(), uri.getPort())
                .handler(new CosmChannelInitializer());
        LOGGER.info("COSM client connecting...");
        Channel ch = cosmBootstrap.connect().sync().channel();
        cosmClientInboundHandler.handshakeFuture().sync();

        return ch;
    }

    private Channel bootstrapRxtx(Channel cosmChannel) throws InterruptedException {
        rxtxBootstrap = new Bootstrap();
        rxtxBootstrap.group(new OioEventLoopGroup())
                .channel(RxtxChannel.class)
                .remoteAddress(new RxtxDeviceAddress(serialPorts.get(0)))
                .option(RxtxChannelOption.BAUD_RATE, baudRate)
                .handler(new RxtxChannelInitializer(cosmChannel));
        LOGGER.info("Listening for serial data on {} at {} bauds...", serialPorts.get(0), baudRate);
        return rxtxBootstrap.connect().sync().channel();
    }

    public class CosmChannelInitializer extends ChannelInitializer<NioSocketChannel> {
        @Override
        public void initChannel(NioSocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast("http-response-decoder", new HttpResponseDecoder());
            pipeline.addLast("http-request-encoder", new HttpRequestEncoder());
            pipeline.addLast("http-aggregator", new HttpObjectAggregator(8192));
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
//            ch.pipeline().addLast("cosm-encoder", new CosmEncoder());
            ch.pipeline().addLast(new ChannelInboundMessageHandlerAdapter<Object>() {
                @Override
                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                    for (int i = 0; i < 10; i++) {
                        String value = ((Double) (Math.random() * 100)).toString();
                        cosmChannel.write(new TextWebSocketFrame("{\n" +
                                "  \"method\" : \"put\",\n" +
                                "  \"resource\" : \"/feeds/98803\",\n" +
                                "  \"params\" : {},\n" +
                                "  \"headers\" : {\"X-ApiKey\":\"" + apiKey + "\"},\n" +
                                "  \"body\" :\n" +
                                "    {\n" +
                                "      \"version\" : \"1.0.0\",\n" +
                                "      \"datastreams\" : [\n" +
                                "        {\n" +
                                "          \"id\" : \"0\",\n" +
                                "          \"current_value\" : \"" + value + "\"\n" +
                                "        }\n" +
                                "      ]\n" +
                                "    },\n" +
                                "  \"token\" : \"0x12345\"\n" +
                                "}\n"));
                        Thread.sleep(5000);
                    }
                }
                @Override
                protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
                }
            });
        }
    }

    public static void main(String[] args) throws Exception {
        XBeeCosm cosm = new XBeeCosm();
        JCommander commander = new JCommander(cosm, args);
        commander.setProgramName("xbee-cosm");

        if (cosm.serialPorts.size() == 0) {
            commander.usage();
            return;
        }

        System.setProperty("gnu.io.rxtx.SerialPorts", cosm.serialPorts.get(0));

        cosm.start();
    }

}
