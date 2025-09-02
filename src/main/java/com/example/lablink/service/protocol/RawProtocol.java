package com.example.lablink.service.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RawProtocol implements Protocol {
    @Override
    public boolean handshake(InputStream in, OutputStream out, boolean isServer) throws IOException {
        // No handshake; successful if streams open
        return true;
    }
}
