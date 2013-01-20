# Examples

### Hello

This example simply sends two XBee frames: one with "hello" and another with "world!".
This is a simple example allowing to check your XBee modules settings.

### Echo

This examples simply echoes what's sent to it.

### COSM

This example acts as a gateway between COSM and XBee modules.
It sends to [COSM](http://www.cosm.com) any received data (it expects doubles as data) via the WebSockets API in order
to update the value of a feed/datastream.