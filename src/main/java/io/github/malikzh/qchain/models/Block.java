package io.github.malikzh.qchain.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malikzh.qchain.enums.BlockVersion;
import lombok.*;

import java.util.List;

import static io.github.malikzh.qchain.utils.Util.sha256;

@Data
@NoArgsConstructor
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

    @SneakyThrows
    public byte[] calculateHash() {
        return sha256(this.toJson());
    }
}
