package com.kalixia.xbee.examples.modeminfo;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.kalixia.xbee.handler.codec.xbee.XBeeFrameEncoder;
import com.kalixia.xbee.handler.codec.xbee.XBeePacketDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.oio.OioEventLoopGroup;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.ByteLoggingHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.logging.InternalLoggerFactory;
import io.netty.logging.Slf4JLoggerFactory;
import io.netty.transport.rxtx.RxtxChannel;
import io.netty.transport.rxtx.RxtxChannelOptions;
import io.netty.transport.rxtx.RxtxDeviceAddress;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
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

                            pipeline.addLast(new ByteLoggingHandler(LogLevel.INFO));

//                            pipeline.addLast(new LineBasedFrameDecoder(80));
//                            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));

                            pipeline.addLast("xbee-frame-decoder", new XBeePacketDecoder());
                            pipeline.addLast("xbee-frame-encoder", new XBeeFrameEncoder());

                            pipeline.addLast("xbee-modem-info-decoder", new XBeeModemInfoDecoder());
                            pipeline.addLast("xbee-modem-info-encoder", new XBeeModemInfoEncoder());

//                            pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                        }
                    });
            LOGGER.info("Listening for serial data on {} at {} bauds...", serialPorts.get(0), baudRate);
            ChannelFuture f = b.connect().sync();

            Channel channel = f.channel();
            channel.closeFuture().sync();
        } finally {
            b.shutdown();
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
