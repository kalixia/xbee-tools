# Examples - Hello

This is a basic example sending two XBee frames: one with a *hello* content and another one with *world!*

Those two XBee frames are broadcasted to all XBee modules.

This simple example allows to check your local XBee module settings.


## How to run

First make sure you have [built the project](../installation.html).
Next, if you have a look at the ``` examples/hello/target/appassembler ``` directory, you will find a complete
*distribution* of the example.

Depending on your OS you either need to run ``` sh bin/xbee-hello ``` or ``` bin\xbee-hello.bat ```.
Do not forget to read the next section as there are some required CLI parameters for this example to work.


## Settings

A few settings are required in order to get the gateway to run properly.
If you launch ``` xbee-hello ``` with no arguments, the usage will be displayed.

Here is below a list of the parameters:

| CLI arg       | Description
|---------------|-------------------------------------------------------------------------------------------------------
| -b            | Bauds (optional, by default 9600)
| *serial port* | Serial port to use: one Windows something like COM3, on Unix machines something like ``` /dev/tty.usbmodem123 ```