package io.github.malikzh.qchain.utils;

import lombok.SneakyThrows;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

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

    public static boolean isLocalAddress(String url) {
        try {
            var u = new URL(url);
            var host = u.getHost();
            return host.startsWith("127.") || host.equalsIgnoreCase("localhost");
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static boolean isCorrectUrl(String url) {
        try {
            var u = new URL(url);
            return u.getProtocol().matches("https?");
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static LocalDateTime parseLocalDateTime(String localDateTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(localDateTime);
        return zonedDateTime.toLocalDateTime();
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
