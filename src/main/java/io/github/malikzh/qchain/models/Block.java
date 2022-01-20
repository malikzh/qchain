package io.github.malikzh.qchain.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malikzh.qchain.enums.BlockVersion;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.List;

@Data
@Builder
public class Block {
    // Версия блока
    private BlockVersion version;

    // Хэш предыдущего блока
    private byte[] prevBlockHash;

    // Корень Меркла
    private byte[] merkleRoot;

    // Дата создания блока
    private String timestamp;

    // Нонс
    private long nonce;

    // Отсортированный список транзакций
    private List<byte[]> transactions;

    @SneakyThrows
    public byte[] toJson() {
        var mapper = new ObjectMapper();

        return mapper.writeValueAsBytes(this);
    }
}
