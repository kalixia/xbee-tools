package com.kalixia.xbee.examples.cosm;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.kalixia.xbee.examples.cosm.http.XBeeCosmGatewayHttp;
import com.kalixia.xbee.examples.cosm.websockets.XBeeCosmGatewayWS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class XBeeCosm {
    @Parameter(description = "The serial port to use for communication with the XBee module")
    public List<String> serialPorts = new ArrayList<String>();

    @Parameter(names = {"-b", "--baud"}, description = "Baud rate")
    private Integer baudRate = 9600;

    @Parameter(names = {"-f", "--feed"}, description = "COSM/Pachube feed ID to which the data should be sent")
    private Long feedID;

    @Parameter(names = {"-d", "--data-stream"}, description = "Data stream to update on the feed")
    private Integer datastreamID;

    @Parameter(names = {"-api"}, description = "API Key for COSM/Pachube", required = true)
    private String apiKey;

    @Parameter(names = {"-ws"}, description = "Use the WebSockets API instead of the HTTP one")
    private boolean useWS;

    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeCosm.class);

    public void start() throws Exception {
        // show debug info
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("About to connect to COSM with API key {}", apiKey);
        }

        XBeeCosmGateway gateway;
        if (useWS)
            gateway = new XBeeCosmGatewayWS();
        else
            gateway = new XBeeCosmGatewayHttp();
        gateway.start(serialPorts.get(0), baudRate, apiKey, feedID, datastreamID);
    }

    public static void main(String[] args) throws Exception {
        XBeeCosm cosm = new XBeeCosm();
        JCommander commander = new JCommander(cosm, args);
        commander.setProgramName("xbee-cosm");

        if (cosm.serialPorts.size() == 0) {
            commander.usage();
            return;
        }

        System.setProperty("gnu.io.rxtx.SerialPorts", cosm.serialPorts.get(0));

        cosm.start();
    }

}
