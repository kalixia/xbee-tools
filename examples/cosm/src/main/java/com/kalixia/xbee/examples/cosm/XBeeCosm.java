package com.kalixia.xbee.examples.cosm;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.kalixia.xbee.api.xbee.RSSI;
import com.kalixia.xbee.api.xbee.XBeeAddress16;
import com.kalixia.xbee.api.xbee.XBeeReceive;
import com.kalixia.xbee.api.xbee.XBeeReceive16;
import com.kalixia.xbee.examples.cosm.client.Cosm;
import com.kalixia.xbee.examples.cosm.client.HttpSocketCosm;
import com.kalixia.xbee.examples.cosm.client.WebSocketCosm;
import com.kalixia.xbee.handler.codec.xbee.XBeeFrameDelimiterDecoder;
import com.kalixia.xbee.handler.codec.xbee.XBeePacketDecoder;
import io.netty.bootstrap.ClientBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPipelineFactory;
import io.netty.channel.Channels;
import io.netty.channel.rxtx.RxtxChannelFactory;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import io.netty.logging.InternalLoggerFactory;
import io.netty.logging.Slf4JLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeCosm.class);

    public void start() throws Exception {
        // configure Netty logging
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());

        // show debug info
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("About to connect to COSM with API key {}", apiKey);
        }

        final ExecutorService executorService = Executors.newCachedThreadPool();

//        final Cosm cosm = new WebSocketCosm(apiKey, feedID, datastreamID, executorService);
        final Cosm cosm = new HttpSocketCosm(apiKey, feedID, datastreamID, executorService);
        cosm.start();

//        LOGGER.info("Sending fake serial event...");
//        cosm.getChannel().write(new XBeeReceive16(
//                new XBeeAddress16(0),
//                new RSSI(0),
//                new XBeeReceive.Options((byte) 0),
//                "25".getBytes("UTF-8")
//        ));

//        cosm.getChannel().write(new TextWebSocketFrame("{\n" +
//                "  \"method\" : \"get\",\n" +
//                "  \"resource\" : \"/feeds/" + feedID + "/datastreams/" + datastreamID + "\",\n" +
//                "  \"headers\" :\n" +
//                "    {\n" +
//                "      \"X-ApiKey\" : \"" + apiKey + "\"\n" +
//                "    },\n" +
//                "  \"token\" : \"0xabcdef\"\n" +
//                "}")).awaitUninterruptibly();

//        cosm.getChannel().write(new TextWebSocketFrame("{\n" +
//                        "  \"method\" : \"subscribe\",\n" +
//                        "  \"resource\" : \"/feeds/" + feedID + "/datastreams/" + datastreamID + "\",\n" +
//                        "  \"headers\" :\n" +
//                        "    {\n" +
//                        "      \"X-ApiKey\" : \"" + apiKey + "\"\n" +
//                        "    },\n" +
//                        "  \"token\" : \"0xabcdef\"\n" +
//                        "}")).awaitUninterruptibly();

        // configure the RXTX client
        RxtxChannelFactory rxtxChannelFactory = new RxtxChannelFactory(executorService);
        ClientBootstrap rxtxBootstrap = new ClientBootstrap(rxtxChannelFactory);

        // set up the RXTX pipeline factory
        rxtxBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("xbee-frame-delimiter", new XBeeFrameDelimiterDecoder());
                pipeline.addLast("xbee-packet-decoder", new XBeePacketDecoder());
                pipeline.addLast("xbee-cosm-relay", new XBeeCosmRelayHandler(cosm));
                return pipeline;
            }
        });
        rxtxBootstrap.setOption("baudrate", baudRate);

        // start the connection attempt
        ChannelFuture rxtxFuture = rxtxBootstrap.connect(new RxtxDeviceAddress(serialPorts.get(0)));

        // wait until the connection is made successfully.
        Channel serialChannel = rxtxFuture.awaitUninterruptibly().getChannel();

        if (!rxtxFuture.isSuccess()) {
            System.err.printf("Can't connect to serial port %s (%s)%n", serialPorts.get(0), rxtxFuture.getCause());
            //rxtxFuture.getCause().printStackTrace();
            cosm.stop();
            System.exit(-1);
        }
        LOGGER.info("Listening for serial data on {} at {} bauds...", serialPorts.get(0), baudRate);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        boolean exit = false;
        while (!exit) {
            try {
                String line = reader.readLine();
                if ("exit".equals(line)) {
                    exit = true;
                }
            } catch (IOException e) {
                // ignore
            }
        }

        // Close the connection.
        cosm.stop();
        serialChannel.close().awaitUninterruptibly();

        // Shut down all thread pools to exit.
        rxtxBootstrap.releaseExternalResources();
    }

    public static void main(String[] args) throws Exception {
        XBeeCosm sniffer = new XBeeCosm();
        JCommander commander = new JCommander(sniffer, args);
        commander.setProgramName("xbee-cosm");

        if (sniffer.serialPorts.size() == 0) {
            commander.usage();
            return;
        }

        System.setProperty("gnu.io.rxtx.SerialPorts", sniffer.serialPorts.get(0));

        sniffer.start();
    }

}
