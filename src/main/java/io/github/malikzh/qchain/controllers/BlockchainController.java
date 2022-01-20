package io.github.malikzh.qchain.controllers;

import io.github.malikzh.qchain.repositories.BlockchainRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.gov.pki.kalkan.util.encoders.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
@RequestMapping(path = "qchain/api/v1/blockchain")
@RequiredArgsConstructor
@Tag(name = "Blockchain", description = "Методы для работы с блокчейном")
public class BlockchainController {
    private final BlockchainRepository blockchainRepository;

    @GetMapping(value = "top", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(tags = "transaction", summary = "Возвращает хэш последнего блока")
    public String top() {
        var hash = blockchainRepository.getLastBlockHash();

        if (Objects.isNull(hash)) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        return Base64.encodeStr(hash);
    }
}
