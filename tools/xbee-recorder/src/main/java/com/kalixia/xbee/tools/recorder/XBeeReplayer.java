package com.kalixia.xbee.tools.recorder;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.kalixia.xbee.handler.codec.xbee.XBeeRequestEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxChannelOption;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import io.netty.channel.socket.oio.OioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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

    public void replay() throws InterruptedException {
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
                            pipeline.addLast("xbee-player", new XBeePlayerHandler(replay));
                            pipeline.addLast("xbee-request-encoder", new XBeeRequestEncoder());
                        }
                    });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
            LOGGER.info("Listening for serial data on {} at {} bauds...", serialPorts.get(0), baudRate);
        } finally {
            b.shutdown();
        }
    }

    public static void main(String[] args) throws InterruptedException {
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

        System.setProperty("gnu.io.rxtx.SerialPorts", recorder.serialPorts.get(0));

        recorder.replay();
    }

}
