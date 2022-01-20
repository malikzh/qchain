package io.github.malikzh.qchain.services;

import io.github.malikzh.qchain.enums.BlockVersion;
import io.github.malikzh.qchain.models.Block;
import io.github.malikzh.qchain.repositories.BlockchainRepository;
import io.github.malikzh.qchain.repositories.PoolRepository;
import kz.gov.pki.kalkan.util.encoders.Hex;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import static io.github.malikzh.qchain.utils.Constants.ZERO_HASH;
import static io.github.malikzh.qchain.utils.Util.sha256;


/**
 * Сервис майнинга (генерации блоков)
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MiningService {
    private final PoolRepository pool;
    private final BlockchainRepository blockchain;

    @Scheduled(cron = "* * * * * *")
    private void generateBlock() {
        log.info("Mining started...");

        var lastBlockHash = blockchain.getLastBlockHash();

        if (pool.getSize() == 0 && Objects.nonNull(lastBlockHash)) {
            log.info("Transaction pool is empty. Skipping");
            return;
        }

        // Создаем  genesis block
        if (Objects.isNull(lastBlockHash)) {
            generateFirstBlock();
            return;
        }


    }

    /**
     * Генератор первого блока
     */
    private void generateFirstBlock() {
        log.info("Generating first block...");

        var genesisBlock = Block.builder()
                .version(BlockVersion.VERSION_1_0)
                .prevBlockHash(ZERO_HASH)
                .merkleRoot(ZERO_HASH)
                .timestamp(LocalDateTime.now().toString())
                .nonce(0)
                .transactions(new ArrayList<>())
                .build()
                .toJson();

        var hash = sha256(genesisBlock);

        blockchain.save(hash, genesisBlock);
        log.info("First block saved with hash: {}", Hex.encodeStr(hash));
    }
}
