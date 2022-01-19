package io.github.malikzh.qchain.models;

import io.github.malikzh.qchain.enums.BlockVersion;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Block {
    // Версия блока
    private BlockVersion version;

    // Хэш предыдущего блока
    private byte[] prevBlockHash;

    // Корень Меркла
    private byte[] merkleRoot;

    // Дата создания блока
    private LocalDateTime timestamp;

    // Нонс
    private long nonce;

    // Отсортированный список транзакций
    private List<Transaction> transactions;
}
