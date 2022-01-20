package io.github.malikzh.qchain.services;

import io.github.malikzh.qchain.enums.BlockVersion;
import io.github.malikzh.qchain.models.Block;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static io.github.malikzh.qchain.utils.Constants.HASH_SIZE;
import static io.github.malikzh.qchain.utils.Util.parseLocalDateTime;

/**
 * Этот сервис содержит в себе алгоритмы для валидации блоков
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BlockchainService {
    /**
     * Валидация
     *
     * @param prevHash
     * @param blocks
     * @return
     */
    public boolean validate(byte[] prevHash, @Nullable LocalDateTime prevTimestamp, List<Block> blocks) {
        if (blocks.size() < 1 || prevHash.length != HASH_SIZE) {
            return false;
        }

        var prev = Arrays.copyOf(prevHash, prevHash.length);

        for (Block block : blocks) {
            // Проверяем версию
            if (!block.getVersion().equals(BlockVersion.VERSION_1_0)) {
                return false;
            }

            // Проверяем хэш
            if (!Arrays.equals(prev, block.getPrevBlockHash())) {
                return false;
            }

            // Проверяем даты
            var remoteTimestamp = parseLocalDateTime(block.getTimestamp());

            if (Objects.nonNull(prevTimestamp)) {
                if (!prevTimestamp.isBefore(remoteTimestamp)) {
                    return false;
                }
            }

            prevTimestamp = remoteTimestamp;

            // Проверка корня Меркле
            // todo

            // Считаем хэш блока и записываем его в prev
            prev = block.calculateHash();
        }

        return true;
    }
}
