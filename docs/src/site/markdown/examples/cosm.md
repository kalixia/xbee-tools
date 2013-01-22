# Examples - COSM

This example acts as a gateway between [COSM](http://www.cosm.com) and XBee modules.

It sends to COSM any received data (it expects doubles as data) in order to update the value of a feed/datastream.

It is actually provided with two different strategies: either using COSM WebSockets API or using the HTTP API.
The WebSockets is a nice way to do this, but proves to be quite unstable on COSM side, hence the provided HTTP version.


## Settings

A few settings are required in order to get the gateway to run properly.
If you launch ``` xbee-cosm ``` with no arguments, the usage will be displayed.

Here is below a list of the parameters:

| CLI arg       | Description
|---------------|-------------------------------------------------------------------------------------------------------
| -b            | Bauds (optional, by default 9600)
| -api          | COSM API Key
| -f            | COSM Feed ID
| -d            | COSM datastream ID (0 usually if you only 1 datastream in your feed)
| -ws           | Switch to WebSockets API (optional, by default use HTTP API -- unfortunately the WS API is quite unreliable on COSM side)
| *serial port* | Serial port to use: one Windows something like COM3, on Unix machines something like ``` /dev/tty.usbmodem123 ```