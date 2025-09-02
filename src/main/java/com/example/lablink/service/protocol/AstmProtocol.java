package com.example.lablink.service.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Minimal ASTM-like ENQ/ACK handshake for demo/testing.
 * ENQ = 0x05, ACK = 0x06, NAK = 0x15
 */
public class AstmProtocol implements Protocol {
    private static final byte ENQ = 0x05;
    private static final byte ACK = 0x06;
    private static final byte NAK = 0x15;

    @Override
    public boolean handshake(InputStream in, OutputStream out, boolean isServer) throws IOException {
        if (isServer) {
            // Wait for ENQ, respond ACK
            int b = in.read();
            if (b == -1) return false;
            if ((byte)b == ENQ) {
                out.write(ACK);
                out.flush();
                return true;
            } else {
                out.write(NAK);
                out.flush();
                return false;
            }
        } else {
            // Client sends ENQ, expects ACK
            out.write(ENQ);
            out.flush();
            int b = in.read();
            return b == ACK;
        }
    }
}
