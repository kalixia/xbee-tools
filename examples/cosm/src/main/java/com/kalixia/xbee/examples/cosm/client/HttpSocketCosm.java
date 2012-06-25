package com.kalixia.xbee.examples.cosm.client;

import com.kalixia.xbee.examples.cosm.codec.json.JsonDecoder;
import com.kalixia.xbee.examples.cosm.codec.json.JsonEncoder;
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
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;

/**
 * <a href="http://www.cosm.com">Cosm</a>/Pachube client based on HTTP Sockets.
 */
public class HttpSocketCosm implements Cosm {
    private Channel cosmChannel;
    private ClientBootstrap httpBootstrap;
    private final String apiKey;
    private final Long feedID;
    private final Integer datastreamID;
    private final ExecutorService executorService;
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpSocketCosm.class);

    public HttpSocketCosm(String apiKey, Long feedID, Integer datastreamID, ExecutorService executorService) {
        this.apiKey = apiKey;
        this.feedID = feedID;
        this.datastreamID = datastreamID;
        this.executorService = executorService;
    }

    @Override
    public void start() throws Exception {
        // configure the WebSockets client
        NioClientSocketChannelFactory websocketsChannelFactory = new NioClientSocketChannelFactory(executorService);
        httpBootstrap = new ClientBootstrap(websocketsChannelFactory);

        // set up the COSM Web Socket pipeline factory
        httpBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("http-encoder", new HttpRequestEncoder());
                pipeline.addLast("cosm-http-handler", getHandler());
                pipeline.addLast("json-encoder", new JsonEncoder());
                pipeline.addLast("string-encoder", new StringEncoder(Charset.forName("UTF-8")));
//                pipeline.addLast("json-decoder", new JsonDecoder());
//                pipeline.addLast("http-decoder", new HttpResponseDecoder());
                return pipeline;
            }
        });

        // start COSM Web Socket connection
        LOGGER.info("COSM client connecting...");
        ChannelFuture httpFuture = httpBootstrap.connect(
                new InetSocketAddress("api.cosm.com", 80));
        httpFuture.awaitUninterruptibly().rethrowIfFailed();
        cosmChannel = httpFuture.getChannel();
    }

    @Override
    public void stop() throws Exception {
        cosmChannel.close().awaitUninterruptibly();
        httpBootstrap.releaseExternalResources();
    }

    @Override
    public Channel getChannel() {
        return cosmChannel;
    }

    @Override
    public ChannelHandler getHandler() {
        return new HttpSocketCosmHandler(apiKey, feedID, datastreamID);
    }
}
