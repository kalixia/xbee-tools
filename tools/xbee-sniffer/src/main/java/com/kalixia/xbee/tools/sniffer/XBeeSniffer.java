package com.kalixia.xbee.tools.sniffer;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.kalixia.xbee.handler.codec.xbee.XBeeFrameDelimiterDecoder;
import com.kalixia.xbee.handler.codec.xbee.XBeePacketDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.oio.OioEventLoopGroup;
import io.netty.logging.InternalLoggerFactory;
import io.netty.logging.Slf4JLoggerFactory;
import io.netty.transport.rxtx.RxtxChannel;
import io.netty.transport.rxtx.RxtxChannelOptions;
import io.netty.transport.rxtx.RxtxDeviceAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class XBeeSniffer {
    @Parameter(description = "The serial port to use for communication with the XBee module")
    public List<String> serialPorts = new ArrayList<String>();

    @Parameter(names = {"-b", "--baud"}, description = "Baud rate")
    private Integer baudRate = 9600;

    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeSniffer.class);

    public void start() throws InterruptedException {
        // Configure Netty logging
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());

        // Configure the client
        Bootstrap b = new Bootstrap();
        try {
            b.group(new OioEventLoopGroup())
                    .channel(RxtxChannel.class)
                    .remoteAddress(new RxtxDeviceAddress(serialPorts.get(0)))
                    .option(RxtxChannelOptions.BAUD_RATE, baudRate)
                    .handler(new ChannelInitializer<RxtxChannel>() {
                        @Override
                        public void initChannel(RxtxChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("xbee-frame-delimiter", new XBeeFrameDelimiterDecoder());
                            pipeline.addLast("xbee-packet-decoder", new XBeePacketDecoder());
                            pipeline.addLast("xbee-request-logger", new XBeeRequestLoggerHandler(false));
//                            pipeline.addLast("string-decoder", new StringDecoder());
//                            pipeline.addLast("logger", new LoggingHandler(true));
                        }
                    });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
            LOGGER.info("Listening for serial data on {} at {} bauds...", serialPorts.get(0), baudRate);
        } finally {
            b.shutdown();
        }

//        if (!channel.isConnected()) {
//            LOGGER.error("Can't connect to serial port {}", serialPort);
//            System.exit(-1);
//        }
    }

    public static void main(String[] args) throws InterruptedException {
//        String serialPort = "/dev/tty.usbserial-A100MZ0L";
        XBeeSniffer sniffer = new XBeeSniffer();
        JCommander commander = new JCommander(sniffer, args);
        commander.setProgramName("xbee-sniffer");

        if (sniffer.serialPorts.size() == 0) {
            commander.usage();
            return;
        }

        System.setProperty("gnu.io.rxtx.SerialPorts", sniffer.serialPorts.get(0));

        sniffer.start();
    }

}
