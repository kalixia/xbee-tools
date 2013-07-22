package com.kalixia.xbee.examples.modeminfo;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.kalixia.xbee.handler.codec.xbee.XBeeFrameDelimiterDecoder;
import com.kalixia.xbee.handler.codec.xbee.XBeePacketDecoder;
import com.kalixia.xbee.handler.codec.xbee.XBeeRequestEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxChannelOption;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Prints information about the locally connected XBee modem.
 */
public class XBeeModemInfo {
    @Parameter(description = "The serial port to use for communication with the XBee module")
    public List<String> serialPorts = new ArrayList<String>();

    @Parameter(names = {"-b", "--baud"}, description = "Baud rate")
    private Integer baudRate = 9600;

    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeModemInfo.class);

    public void info() throws InterruptedException {
        // Configure the client
        Bootstrap b = new Bootstrap();
        try {
            b.group(new OioEventLoopGroup())
                    .channel(RxtxChannel.class)
                    .remoteAddress(new RxtxDeviceAddress(serialPorts.get(0)))
                    .option(RxtxChannelOption.BAUD_RATE, baudRate)
                    .handler(new ChannelInitializer<RxtxChannel>() {
                        @Override
                        public void initChannel(RxtxChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast("xbee-request-encoder", new XBeeRequestEncoder());
                            pipeline.addLast("xbee-frame-decoder", new XBeeFrameDelimiterDecoder());
                            pipeline.addLast("xbee-packet-decoder", new XBeePacketDecoder(1));
//                            pipeline.addLast("xbee-modem-info-encoder", new XBeeModemInfoEncoder());
                            pipeline.addLast("xbee-modem-info-decoder", new XBeeModemInfoDecoder());
                        }
                    });
            LOGGER.info("Listening for serial data on {} at {} bauds...", serialPorts.get(0), baudRate);
            ChannelFuture f = b.connect().sync();

            Channel channel = f.channel();
            channel.closeFuture().sync();
        } finally {
            b.group().shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        String serialPort = "/dev/tty.usbserial-A100MZ0L";
        XBeeModemInfo info = new XBeeModemInfo();
        JCommander commander = null;
        try {
            commander = new JCommander(info, args);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        commander.setProgramName("info");

        if (info.serialPorts.size() == 0) {
            commander.usage();
            System.exit(-1);
        }

        System.setProperty("gnu.io.rxtx.SerialPorts", info.serialPorts.get(0));

        info.info();
    }

}
