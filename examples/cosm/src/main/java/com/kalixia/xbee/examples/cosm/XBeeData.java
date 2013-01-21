package com.kalixia.xbee.examples.cosm;

public class XBeeData {
    private final String apiKey;
    private final Long feedID;
    private final Integer datastreamID;
    private final Double value;

    public XBeeData(String apiKey, Long feedID, Integer datastreamID, Double value) {
        this.apiKey = apiKey;
        this.feedID = feedID;
        this.datastreamID = datastreamID;
        this.value = value;
    }

    public String getApiKey() {
        return apiKey;
    }

    public Long getFeedID() {
        return feedID;
    }

    public Integer getDatastreamID() {
        return datastreamID;
    }

    public Double getValue() {
        return value;
    }
}
