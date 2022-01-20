package io.github.malikzh.qchain.services;

import io.github.malikzh.qchain.configurations.QChainConfiguration;
import io.github.malikzh.qchain.exceptions.TransactionPoolException;
import io.github.malikzh.qchain.repositories.PoolRepository;
import kz.gov.pki.kalkan.util.encoders.Hex;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static io.github.malikzh.qchain.utils.Util.sha256;

/**
 * Пул транзакций.
 *
 * Данный сервис отвечает за прием транзакций и хранение их в пуле.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionPoolService {
    private final QChainConfiguration configuration;
    private final PoolRepository repository;

    /**
     * Добавление транзакции в пул
     * @param xml
     * @return
     */
    public String add(String xml) throws TransactionPoolException {
        var key = sha256(xml);

        if (configuration.getMaxPoolSize() > 0 && repository.getSize() >= configuration.getMaxPoolSize()) {
            throw new TransactionPoolException(HttpStatus.TOO_MANY_REQUESTS, "Пул тразнакций переполнен.");
        }

        if (Objects.nonNull(repository.find(key))) {
            throw new TransactionPoolException(HttpStatus.CONFLICT, "Данная транзакция уже существует в пуле.");
        }

        repository.save(key, xml);

        return Hex.encodeStr(key);
    }
}
