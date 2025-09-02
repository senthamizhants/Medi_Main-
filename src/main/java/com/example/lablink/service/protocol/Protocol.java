package com.example.lablink.service.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Protocol {
    /** Optional handshake for test connection. */
    boolean handshake(InputStream in, OutputStream out, boolean isServer) throws IOException;

    /** Send a message payload (string for demo). */
    default void send(OutputStream out, String payload) throws IOException {
        out.write(payload.getBytes());
        out.flush();
    }
}
