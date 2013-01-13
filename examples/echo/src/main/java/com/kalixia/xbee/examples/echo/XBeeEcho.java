package com.kalixia.xbee.examples.echo;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.kalixia.xbee.handler.codec.xbee.XBeeFrameDelimiterDecoder;
import com.kalixia.xbee.handler.codec.xbee.XBeeFrameEncoder;
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

public class XBeeEcho {
    @Parameter(description = "The serial port to use for communication with the XBee module")
    public List<String> serialPorts = new ArrayList<String>();

    @Parameter(names = {"-b", "--baud"}, description = "Baud rate")
    private Integer baudRate = 9600;

    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeEcho.class);

    public void echo() throws InterruptedException {
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
                            pipeline.addLast("xbee-packet-encoder", new XBeeFrameEncoder());
                            pipeline.addLast("xbee-echo", new XBeeEchoHandler());
                        }
                    });
            LOGGER.info("Listening for serial data on {} at {} bauds...", serialPorts.get(0), baudRate);
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        } finally {
            b.shutdown();
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        String serialPort = "/dev/tty.usbserial-A100MZ0L";
        XBeeEcho echo = new XBeeEcho();
        JCommander commander = null;
        try {
            commander = new JCommander(echo, args);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        commander.setProgramName("echo");

        if (echo.serialPorts.size() == 0) {
            commander.usage();
            System.exit(-1);
        }

        System.setProperty("gnu.io.rxtx.SerialPorts", echo.serialPorts.get(0));

        echo.echo();
    }

}
