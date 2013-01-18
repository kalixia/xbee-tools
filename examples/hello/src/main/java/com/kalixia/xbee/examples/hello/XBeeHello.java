package com.kalixia.xbee.examples.hello;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.kalixia.xbee.handler.codec.xbee.XBeeRequestEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxChannelOption;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import io.netty.channel.socket.oio.OioEventLoopGroup;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.ByteLoggingHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.logging.InternalLoggerFactory;
import io.netty.logging.Slf4JLoggerFactory;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Sends two XBee frames as broadcast, so any receiver should receive it.
 * The first message is <tt>hello</tt>, the second one <tt>world!</tt>.
 * The example exits after sending those two messages.
 */
public class XBeeHello {
    @Parameter(description = "The serial port to use for communication with the XBee module")
    public List<String> serialPorts = new ArrayList<String>();

    @Parameter(names = {"-b", "--baud"}, description = "Baud rate")
    private Integer baudRate = 9600;

    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeHello.class);

    public void hello() throws InterruptedException {
        // Configure Netty logging
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());

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
                            pipeline.addLast(new ByteLoggingHandler(LogLevel.INFO));

                            pipeline.addLast("xbee-request-encoder", new XBeeRequestEncoder());

                            pipeline.addLast(new LineBasedFrameDecoder(80));
                            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));

                            pipeline.addLast("xbee-hello-encoder", new XBeeHelloEncoder());
                            pipeline.addLast("xbee-hello-decoder", new XBeeHelloDecoder());
                        }
                    });
            LOGGER.info("Listening for serial data on {} at {} bauds...", serialPorts.get(0), baudRate);
            ChannelFuture f = b.connect().sync();

            Channel channel = f.channel();

//            LOGGER.info("Sending first message");
//            channel.write("hello\n");
//            Thread.sleep(5000);
//            LOGGER.info("Sending second message");
//            channel.write("world!\n");

//            LOGGER.info("Exiting example application now");
//            channel.write("exit\n").sync();

            channel.closeFuture().sync();
        } finally {
            b.shutdown();
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        String serialPort = "/dev/tty.usbserial-A100MZ0L";
        XBeeHello hello = new XBeeHello();
        JCommander commander = null;
        try {
            commander = new JCommander(hello, args);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        commander.setProgramName("hello");

        if (hello.serialPorts.size() == 0) {
            commander.usage();
            System.exit(-1);
        }

        System.setProperty("gnu.io.rxtx.SerialPorts", hello.serialPorts.get(0));

        hello.hello();
    }

}
