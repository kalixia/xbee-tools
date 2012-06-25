package com.kalixia.xbee.examples.cosm.client;

import io.netty.bootstrap.ClientBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPipelineFactory;
import io.netty.channel.Channels;
import io.netty.channel.socket.nio.NioClientSocketChannelFactory;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

/**
 * <a href="http://www.cosm.com">Cosm</a>/Pachube client based on Web Sockets.
 */
public class WebSocketCosm implements Cosm {
    private Channel cosmChannel;
    private ClientBootstrap websocketsBootstrap;
    private final String apiKey;
    private final ExecutorService executorService;
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketCosm.class);

    public WebSocketCosm(String apiKey, ExecutorService executorService) {
        this.apiKey = apiKey;
        this.executorService = executorService;
    }

    @Override
    public void start() throws Exception {
        // configure the WebSockets client
        NioClientSocketChannelFactory websocketsChannelFactory = new NioClientSocketChannelFactory(executorService);
        websocketsBootstrap = new ClientBootstrap(websocketsChannelFactory);
        HashMap<String, String> cosmHeaders = new HashMap<String, String>();
        cosmHeaders.put("X-ApiKey", apiKey);
        URI cosmApiURI = new URI("ws://api.cosm.com:8080");
        final WebSocketClientHandshaker handshaker =
                new WebSocketClientHandshakerFactory().newHandshaker(
                        cosmApiURI, WebSocketVersion.V13, null, false, cosmHeaders);

        // set up the COSM Web Socket pipeline factory
        websocketsBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder", new HttpResponseDecoder());
                pipeline.addLast("encoder", new HttpRequestEncoder());
                pipeline.addLast("cosm-ws-handler", getHandler());
                return pipeline;
            }
        });

        // start COSM Web Socket connection
        LOGGER.info("COSM client connecting...");
        ChannelFuture websocketsFuture = websocketsBootstrap.connect(
                new InetSocketAddress(cosmApiURI.getHost(), cosmApiURI.getPort()));
        websocketsFuture.awaitUninterruptibly().rethrowIfFailed();
        cosmChannel = websocketsFuture.getChannel();
        handshaker.handshake(cosmChannel).awaitUninterruptibly().rethrowIfFailed();
    }

    @Override
    public void stop() throws Exception {
        cosmChannel.close().awaitUninterruptibly();
        websocketsBootstrap.releaseExternalResources();
    }

    @Override
    public Channel getChannel() {
        return cosmChannel;
    }

    @Override
    public ChannelHandler getHandler() {
        return new WebSocketCosmHandler(getChannel());
    }
}
