package com.kalixia.xbee.api.xbee;

import com.kalixia.xbee.utils.HexUtils;

import java.io.Serializable;

public class XBeeAtCommandResponse {
    private final byte frameID;
    private final String command;
    private final Status status;
    private final byte[] data;

    public XBeeAtCommandResponse(byte frameID, String command, Status status, byte[] data) {
        this.frameID = frameID;
        this.command = command;
        this.status = status;
        this.data = data;
    }

    public byte getFrameID() {
        return frameID;
    }

    public String getCommand() {
        return command;
    }

    public Status getStatus() {
        return status;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "XBeeAtCommandResponse{" +
                "frameID=" + frameID +
                ", command='" + command + '\'' +
                ", status=" + status +
                ", data=" + HexUtils.toHexStringPrefixed(data) +
                '}';
    }

    public enum Status implements Serializable {
        OK(0), ERROR(1), INVALID_COMMAND(2), INVALID_PARAMETER(3);

        private final int value;

        Status(int value) {
            this.value = value;
        }

        public static Status fromValue(int value) {
            for (Status status : values())
                if (status.value == value)
                    return status;
            throw new IllegalArgumentException("Value " + value + " is not valid");
        }
    }
}
