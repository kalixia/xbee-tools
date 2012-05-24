package com.kalixia.xbee.tools.recorder;

public enum Format {
    STRING("string"), HEX("hex");

    private String format;

    Format(String format) {
        this.format = format;
    }

    public static Format from(String format) {
        for (Format f : values())
            if (f.format.equalsIgnoreCase(format))
                return f;
        throw new IllegalArgumentException(String.format("Unknown format '%s'", format));
    }
}
