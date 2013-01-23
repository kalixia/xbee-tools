# Examples - Hello

This is a basic example sending two XBee frames: one with a *hello* content and another one with *world!*

Those two XBee frames are broadcasted to all XBee modules.

This simple example allows to check your local XBee module settings.


## Build

First make sure you have [built the project](../installation.html).
Next, if you have a look at the ``` examples/hello/target/appassembler ``` directory, you will find a complete
*distribution* of the example.


## Configure

A few settings are required in order to get the gateway to run properly.
If you launch ``` xbee-hello ``` with no arguments, the usage will be displayed.

Here is below a list of the parameters:

| CLI arg       | Description
|---------------|-------------------------------------------------------------------------------------------------------
| -b            | Bauds (optional, by default 9600)
| *serial port* | Serial port to use: one Windows something like COM3, on Unix machines something like ``` /dev/tty.usbmodem123 ```


## Run

Depending on your OS you either need to run ``` sh bin/xbee-hello ``` or ``` bin\xbee-hello.bat ```.
Do not forget the required CLI parameters for this example to work.


## Understand

This library provides you a Netty handler which is able to encode ``` XBeeRequest ``` objects as expected by the XBee
module (using API mode).

So in order to send some basic broadcasted messages, you simply need to write a Netty handler which encodes
``` String ``` objects into ``` XBeeTransmit16 ``` objects:

```
public class XBeeHelloEncoder extends MessageToMessageEncoder<String> {
  @Override
  protected Object encode(ChannelHandlerContext ctx, String msg) throws Exception {
    return new XBeeTransmit16(XBeeFrameIdGenerator.nextFrameID(),
                              XBeeAddress16.BROADCAST,
                              new XBeeTransmit.Options(false, false),
                              msg.getBytes("UTF-8"));
    }
}
```

The way to use this encoder can be illustrated here. You simply write ``` String ``` objects in Netty channel and
your Netty handler will convert them to ``` XBeeRequest ``` objects:

```
Bootstrap b = new Bootstrap();
b.group(new OioEventLoopGroup())
  .channel(RxtxChannel.class)
  .remoteAddress(new RxtxDeviceAddress(serialPort)
  .option(RxtxChannelOption.BAUD_RATE, baudRate)
  .handler(new ChannelInitializer<RxtxChannel>() {
    @Override
    public void initChannel(RxtxChannel ch) throws Exception {
      ChannelPipeline pipeline = ch.pipeline();
      pipeline.addLast("xbee-request-encoder", new XBeeRequestEncoder());
      pipeline.addLast("xbee-hello-encoder", new XBeeHelloEncoder());
      [...]
    }
  });
  ChannelFuture f = b.connect().sync();
  [...]
  Channel channel = f.channel();
  channel.write("hello\n");
  channel.write("world!\n");
  // wait a little bit for messages to be sent
  Thread.sleep(5000);
  channel.closeFuture().sync();
```