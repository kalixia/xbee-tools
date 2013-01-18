# XBee/ZigBee Java Tools

XBee/ZigBee Java API and tools based on [Netty](http://www.netty.io).

*This stuff is still work in progress. Highly unstable right now!*

This library can be used at different levels:
* on a high level, this is a Java API for XBee modules,
* on a low level, this is a bunch of Netty handlers allowing you to easily create gateways
(for example Serial -> Cloud via HTTP calls).

## Tools

### XBee Sniffer

This utility displays the received XBee packets on the console.

### XBee recorder/player

This utility can record received data packets and replay them.

## Pre-requisites

[Netty](http://www.netty.io) 4.0-Beta-1 at least (not released yet -- build it from Git)


## Notes

Please note that only XBee/ZigBee modules configured to use API level 1 works with these tools.
