package io.github.malikzh.qchain.services;

import io.github.malikzh.qchain.repositories.PoolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


/**
 * Сервис майнинга (генерации блоков)
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MiningService {
    private final PoolRepository poolRepository;

    @Scheduled(cron = "* * * * * *")
    private void generateBlock() {
        log.debug("DB Size: {}", poolRepository.getSize());

        log.debug("Mining new block...");
    }
}
