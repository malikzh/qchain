package io.github.malikzh.qchain.utils;

import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Util {
    @SneakyThrows
    public static byte[] sha256(String data) {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        return messageDigest.digest(data.getBytes(StandardCharsets.UTF_8));
    }

    @SneakyThrows
    public static byte[] sha256(byte[] data) {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        return messageDigest.digest(data);
    }
}
