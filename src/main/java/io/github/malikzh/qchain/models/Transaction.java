package io.github.malikzh.qchain.models;

import lombok.Builder;
import lombok.Data;

import static io.github.malikzh.qchain.utils.Util.sha256;

@Data
@Builder
public class Transaction {
    // Основной xml
    private String payload;

    public byte[] getSha256Hash() {
        return sha256(payload);
    }
}
