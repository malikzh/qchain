package io.github.malikzh.qchain.services;

import io.github.malikzh.qchain.configurations.QChainConfiguration;
import io.github.malikzh.qchain.enums.BlockVersion;
import io.github.malikzh.qchain.models.Block;
import io.github.malikzh.qchain.repositories.BlockchainRepository;
import io.github.malikzh.qchain.repositories.PoolRepository;
import io.github.malikzh.qchain.utils.Constants;
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
    private final QChainConfiguration config;

    @Scheduled(cron = "0 * * * * *")
    private void generateBlock() {
        log.info("Mining started...");

        var lastBlockHash = blockchain.getLastBlockHash();

        if (pool.getSize() == 0 && Objects.nonNull(lastBlockHash)) {
            log.info("Transaction pool is empty. Skipping...");
            return;
        }

        // Создаем  genesis block
        if (Objects.isNull(lastBlockHash)) {
            if (!config.getCreateGenesisBlock()) {
                log.info("Wait for genesis block from another nodes...");
                return;
            }

            generateFirstBlock();
            return;
        }

        // Список транзакций, которые попадут в блок
        var transactions = pool.get(Constants.MINING_MAX_POOL_SIZE);

        if (Objects.isNull(transactions) || transactions.size() < Constants.MINING_MIN_POOL_SIZE) {
            log.info("Not enough transactions quantity to make new block");
            return;
        }

        // Подбираем nonce
        for (long nonce = 0L; nonce < Long.MAX_VALUE; ++nonce) {
            var block = new Block();
            block.setVersion(BlockVersion.VERSION_1_0);
            block.setPrevBlockHash(lastBlockHash);
            block.setMerkleRoot(ZERO_HASH); // todo
            block.setTimestamp(LocalDateTime.now().toString());
            block.setNonce(nonce);
            block.setTransactions(transactions);

            var blockHash = block.calculateHash();

            if (blockHash[0] == 0x00) {
                blockchain.save(blockHash, block.toJson());

                // Удаляем транзакции, которые попали в блок
                transactions.forEach((item) -> pool.delete(sha256(item)));

                log.info("Mined new block '{}'", Hex.encodeStr(blockHash));
                break;
            }
        }

    }

    /**
     * Генератор первого блока
     */
    private void generateFirstBlock() {
        log.info("Generating first block...");

        var genesisBlock = new Block();
        genesisBlock.setVersion(BlockVersion.VERSION_1_0);
        genesisBlock.setPrevBlockHash(ZERO_HASH);
        genesisBlock.setMerkleRoot(ZERO_HASH);
        genesisBlock.setTimestamp(LocalDateTime.now().toString());
        genesisBlock.setNonce(0);
        genesisBlock.setTransactions(new ArrayList<>());

        var hash = genesisBlock.calculateHash();

        blockchain.save(hash, genesisBlock.toJson());
        log.info("First block saved with hash: {}", Hex.encodeStr(hash));
    }
}
