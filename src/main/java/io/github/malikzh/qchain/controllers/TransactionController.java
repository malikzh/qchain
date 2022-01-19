package io.github.malikzh.qchain.controllers;

import io.github.malikzh.qchain.models.Transaction;
import io.github.malikzh.qchain.services.TransactionPoolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "qchain/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "Методы для работы с транзакциями")
public class TransactionController {
    final TransactionPoolService transactionPoolService;

    /**
     * Добавление транзакции в пул транзакций.
     *
     * @param xml
     * @return
     */
    @PostMapping(value = "", consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(tags = "transaction", summary = "Добавляет транзакцию в пул для дальнейшей обработки. " +
            "Транзакция - это подписанный XML (при помощи библиотеки kalkancrypt), содержащий определённые данные.")
    public ResponseEntity<Transaction> add(@RequestBody String xml) {
        return new ResponseEntity<>(transactionPoolService.add(xml), HttpStatus.CREATED);
    }
}
