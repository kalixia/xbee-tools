package com.kalixia.xbee.tools.recorder;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.kalixia.xbee.handler.codec.xbee.XBeeFrameDelimiterDecoder;
import com.kalixia.xbee.handler.codec.xbee.XBeePacketDecoder;
import com.kalixia.xbee.handler.codec.xbee.XBeePacketEncoder;
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

public class XBeeReplayer {
    @Parameter(description = "The serial port to use for communication with the XBee module")
    public List<String> serialPorts = new ArrayList<String>();

    @Parameter(names = {"-b", "--baud"}, description = "Baud rate")
    private Integer baudRate = 9600;

    @Parameter(names = {"-s", "--source"}, description = "Recorded file to replay")
    private String replay;

//    @Parameter(names = {"-f", "--format"}, description = "Format of the data: either 'string' or 'hex'",
//            converter = FormatConverter.class)
//    private Format format = Format.STRING;

    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeReplayer.class);

    public void replay() {
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

                pipeline.addLast("xbee-player", new XBeePlayerHandler(replay));
                pipeline.addLast("xbee-packet-encoder", new XBeePacketEncoder());
//                pipeline.addLast("xbee-frame-delimiter", new XBeeFrameDelimiterDecoder());
//                pipeline.addLast("xbee-packet-decoder", new XBeePacketDecoder());

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
        XBeeReplayer recorder = new XBeeReplayer();
        JCommander commander = null;
        try {
            commander = new JCommander(recorder, args);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        commander.setProgramName("xbee-replayer");

        if (recorder.serialPorts.size() == 0) {
            commander.usage();
            System.exit(-1);
        }
        if (recorder.replay == null) {
            commander.usage();
            System.exit(-1);
        }

        recorder.replay();
    }

}
