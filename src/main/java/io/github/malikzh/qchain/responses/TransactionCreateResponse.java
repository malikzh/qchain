package io.github.malikzh.qchain.responses;

import io.github.malikzh.qchain.enums.TransactionStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionCreateResponse {
    private String hash;
    private TransactionStatus status;
}
