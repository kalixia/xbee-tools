package com.kalixia.xbee.tools.recorder;

import com.beust.jcommander.IStringConverter;

public class FormatConverter implements IStringConverter<Format> {
    public Format convert(String s) {
        return Format.from(s);
    }
}
