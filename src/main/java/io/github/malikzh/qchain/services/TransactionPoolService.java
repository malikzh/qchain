package io.github.malikzh.qchain.services;

import io.github.malikzh.qchain.exceptions.TransactionPoolException;
import io.github.malikzh.qchain.models.Transaction;
import io.github.malikzh.qchain.repositories.PoolRepository;
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
    private final PoolRepository repository;

    /**
     * Добавление транзакции в пул
     * @param xml
     * @return
     */
    public Transaction add(String xml) throws TransactionPoolException {
        var key = sha256(xml);

        if (Objects.nonNull(repository.find(key))) {
            throw new TransactionPoolException(HttpStatus.CONFLICT, "Данная транзакция уже существует в пуле.");
        }

        repository.save(key, xml);

        return null;
    }
}