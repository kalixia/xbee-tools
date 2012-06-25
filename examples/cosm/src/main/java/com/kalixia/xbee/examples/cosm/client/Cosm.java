package com.kalixia.xbee.examples.cosm.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

/**
 * <a href="http://www.cosm.com">Cosm</a>/Pachube client.
 */
public interface Cosm {
    void start() throws Exception;
    void stop() throws Exception;
    Channel getChannel();
    ChannelHandler getHandler();
}
