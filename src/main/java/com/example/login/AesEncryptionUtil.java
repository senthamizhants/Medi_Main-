package com.example.login;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.spec.KeySpec;
import java.util.Base64;

@Component
public class AesEncryptionUtil {

    private static String ENCRYPTION_KEY;

    private static final byte[] SALT = new byte[] {
        0x49, 0x76, 0x61, 0x6e,
        0x20, 0x4d, 0x65, 0x64,
        0x76, 0x65, 0x64, 0x65,
        0x76
    };

    @Value("${aes.secret.key}")
    private String secretKeyFromConfig;

    @PostConstruct
    public void init() {
        ENCRYPTION_KEY = secretKeyFromConfig;
    }

    public static String encrypt(String plainText) throws Exception {
        SecretKeySpec secret = getSecretKey();
        IvParameterSpec iv = new IvParameterSpec(secret.getEncoded(), 0, 16);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret, iv);
        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-16LE"));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encryptedText) throws Exception {
        SecretKeySpec secret = getSecretKey();
        IvParameterSpec iv = new IvParameterSpec(secret.getEncoded(), 0, 16);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, iv);
        byte[] decoded = Base64.getDecoder().decode(encryptedText);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted, "UTF-16LE");
    }

    private static SecretKeySpec getSecretKey() throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(ENCRYPTION_KEY.toCharArray(), SALT, 1000, 256);
        byte[] key = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(key, "AES");
    }
}
