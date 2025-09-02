package com.example.ServerClientApp;

public class ASTMUtils {
    public static String calculateChecksum(String msg) {
        int sum = 0;
        for (char c : msg.toCharArray()) {
            sum += (int) c;
        }
        String hex = Integer.toHexString(sum);
        return hex.substring(hex.length() - 2).toUpperCase();
    }
}
