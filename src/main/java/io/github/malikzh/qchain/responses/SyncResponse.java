package io.github.malikzh.qchain.responses;

import io.github.malikzh.qchain.models.Block;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SyncResponse {

    // Хэш последнего нашего блока
    private String top;

    // Список блоков
    private List<Block> blocks;
}
