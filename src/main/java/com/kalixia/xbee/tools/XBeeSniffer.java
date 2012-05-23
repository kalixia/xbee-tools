package com.kalixia.xbee.tools;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
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
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LoggingHandler;
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

public class XBeeSniffer {
    @Parameter(description = "The serial port to use for communication with the XBee module")
    public List<String> serialPorts = new ArrayList<String>();

    @Parameter(names = {"-b", "--baud"}, description = "Baud rate")
    private Integer baudRate = 9600;

    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeSniffer.class);

    public void start() {
        // Configure Netty logging
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());

                // Configure the client.
        final ExecutorService executorService = Executors.newCachedThreadPool();
        RxtxChannelFactory rxtxChannelFactory = new RxtxChannelFactory(executorService);
        ClientBootstrap bootstrap = new ClientBootstrap(rxtxChannelFactory);

        // Set up the pipeline factory.
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                // Create and configure a new pipeline for a new channel.
                ChannelPipeline pipeline = Channels.pipeline();

                pipeline.addLast("xbee-frame-delimiter", new XBeeFrameDelimiterDecoder());
                pipeline.addLast("xbee-packet-decoder", new XBeePacketDecoder());

                pipeline.addLast("xbee-request-logger", new XBeeRequestLoggerHandler());

//                pipeline.addLast("string-decoder", new StringDecoder());
//                pipeline.addLast("logger", new LoggingHandler(true));
                return pipeline;
            }
        });
        bootstrap.setOption("baudrate", baudRate);

        // Start the connection attempt.
        ChannelFuture future = bootstrap.connect(new RxtxDeviceAddress(serialPorts.get(0)));

        // Wait until the connection is made successfully.
        Channel channel = future.awaitUninterruptibly().getChannel();

//        if (!channel.isConnected()) {
//            LOGGER.error("Can't connect to serial port {}", serialPort);
//            System.exit(-1);
//        }
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
        channel.close().awaitUninterruptibly();

        // Shut down all thread pools to exit.
        bootstrap.releaseExternalResources();
    }

    public static void main(String[] args) {
//        String serialPort = "/dev/tty.usbserial-A100MZ0L";
        XBeeSniffer sniffer = new XBeeSniffer();
        JCommander commander = new JCommander(sniffer, args);
        commander.setProgramName("xbee-sniffer");

        if (sniffer.serialPorts.size() == 0) {
            commander.usage();
            return;
        }

        sniffer.start();
    }

}
