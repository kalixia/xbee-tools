package com.kalixia.xbee.api.xbee;

import java.util.concurrent.TimeUnit;

public interface XBeeFuture {
    XBeeResponse getResponse();

    XBeeFuture addListener(XBeeFutureListener listener);
    XBeeFuture removeListener(XBeeFutureListener listener);

    XBeeFuture waitForAck(long duration, TimeUnit unit) throws InterruptedException;
    XBeeFuture waitUninterruptibly(long duration, TimeUnit unit) throws InterruptedException;
}
