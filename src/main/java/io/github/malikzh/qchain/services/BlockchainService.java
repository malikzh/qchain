package io.github.malikzh.qchain.services;

import io.github.malikzh.qchain.enums.BlockVersion;
import io.github.malikzh.qchain.models.Block;
import io.github.malikzh.qchain.repositories.BlockchainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final BlockchainRepository blockchainRepository;


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

    /**
     * Возвращает список блоков, до блока с хэшем == hash (не включая сам этот блок)
     * @param hash
     * @return
     */
    public List<Block> getBlocks(byte[] hash) {
        if (Objects.isNull(hash) || hash.length != HASH_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid hash");
        }

        byte[] localTop = blockchainRepository.getLastBlockHash();

        // Если наш блокчейн пуст, мы не принимаем цепочки ни от кого
        if (Objects.isNull(localTop)) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Blockchain of this node is empty");
        }

        var depth = blockchainRepository.getDepthTo(hash);

        // Вытаскиваем блоки
        if (depth < 0) {
            return new ArrayList<>();
        }

        return blockchainRepository.getBlocksFromTop(depth);
    }
}
