package io.github.malikzh.qchain.models;

import io.github.malikzh.qchain.enums.TransactionStatus;
import lombok.Data;

import static io.github.malikzh.qchain.utils.Util.sha256;

@Data
public class Transaction {
    // Основной xml
    private String payload;

    private TransactionStatus status;

    public byte[] getSha256Hash() {
        return sha256(payload + status.name());
    }
}
