# Examples - Echo

This examples sends back any data received.

It can be useful for setup of your XBee modules, in order to make sure they can communicate to each other.

## Build

First make sure you have [built the project](../installation.html).
Next, if you have a look at the ``` examples/echo/target/appassembler ``` directory, you will find a complete
*distribution* of the example.


## Configure

A few settings are required in order to get the gateway to run properly.
If you launch ``` xbee-echo ``` with no arguments, the usage will be displayed.

Here is below a list of the parameters:

| CLI arg       | Description
|---------------|-------------------------------------------------------------------------------------------------------
| -b            | Bauds (optional, by default 9600)
| *serial port* | Serial port to use: one Windows something like COM3, on Unix machines something like ``` /dev/tty.usbmodem123 ```


## Run

Depending on your OS you either need to run ``` sh bin/xbee-echo ``` or ``` bin\xbee-echo.bat ```.
Do not forget the required CLI parameters for this example to work.


## Understand

This library provides you a Netty handler which is able to decode data from the serial port (using API mode) into
``` XBeeReceive ``` objects.

So in order to reply to the XBee module which sent data, you simply need to write a Netty handler which handles
``` XBeeReceive ``` objects and write some ``` XBeeTransmit16 ``` objects in the pipeline:

```
public class XBeeEchoHandler extends ChannelInboundMessageHandlerAdapter<XBeeReceive> {
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, XBeeReceive request)
            throws Exception {
        XBeeTransmit16 response = new XBeeTransmit16(
                        XBeeFrameIdGenerator.nextFrameID(),
                        (XBeeAddress16) request.getSource(),
                        new XBeeTransmit.Options(false, false),
                        request.getData());
         ctx.write(response);
    }

}
```

The way to use this handler can be illustrated here. You simply wait for ``` XBeeReceive ``` objects and write back
``` XBeeRequest ``` objects:

```
Bootstrap b = new Bootstrap();
b.group(new OioEventLoopGroup())
  .channel(RxtxChannel.class)
  .remoteAddress(new RxtxDeviceAddress(serialPort)
  .option(RxtxChannelOption.BAUD_RATE, baudRate)
  .handler(new ChannelInitializer<RxtxChannel>() {
    @Override
    public void initChannel(RxtxChannel ch) throws Exception {
      pipeline.addLast("xbee-frame-delimiter", new XBeeFrameDelimiterDecoder());
      pipeline.addLast("xbee-packet-decoder", new XBeePacketDecoder(1));
      pipeline.addLast("xbee-request-encoder", new XBeeRequestEncoder());
      pipeline.addLast("xbee-echo", new XBeeEchoHandler());
      [...]
    }
  });
  ChannelFuture f = b.connect().sync();
  // keeps running until you force the program to exit
  channel.closeFuture().sync();
```