package com.kalixia.xbee.tools;

import io.netty.bootstrap.ClientBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPipelineFactory;
import io.netty.channel.Channels;
import io.netty.channel.rxtx.RxtxChannelFactory;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import io.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.frame.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.logging.InternalLoggerFactory;
import io.netty.logging.Slf4JLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class XBeeSniffer {
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeSniffer.class);

    public XBeeSniffer(String serialPort, int baudrate) {
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
                pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                pipeline.addLast("decoder", new StringDecoder());
//                pipeline.addLast("encoder", new StringEncoder());
//                pipeline.addLast("logger", new LoggingHandler(true));
                pipeline.addLast("xbee-logger", new XBeeLoggerHandler());
                return pipeline;
            }
        });
        bootstrap.setOption("baudrate", baudrate);

        // Start the connection attempt.
        ChannelFuture future = bootstrap.connect(new RxtxDeviceAddress(serialPort));

        // Wait until the connection is made successfully.
        Channel channel = future.awaitUninterruptibly().getChannel();

//        if (!channel.isConnected()) {
//            LOGGER.error("Can't connect to serial port {}", serialPort);
//            System.exit(-1);
//        }
        LOGGER.info("Listening for serial data on {} at {} bauds...", serialPort, baudrate);

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
//        String serialPort = "/dev/cu.usbserial-A100MZ0L";
        String serialPort = "/dev/tty.usbserial-A100MZ0L";
        LOGGER.info("Starting listener on serial port {}", serialPort);
        new XBeeSniffer(serialPort, 9600);
    }

}
