package io.github.malikzh.qchain.requests;

import io.github.malikzh.qchain.models.Block;
import lombok.Data;

import java.util.List;

@Data
public class SyncRequest {

    // Хэш топового блока удалённой ноды
    private String top;

    // Список блоков
    private List<Block> blocks;
}
