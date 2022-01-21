package io.github.malikzh.qchain.controllers;

import io.github.malikzh.qchain.models.Block;
import io.github.malikzh.qchain.repositories.BlockchainRepository;
import io.github.malikzh.qchain.services.BlockchainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.gov.pki.kalkan.util.encoders.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "qchain/api/v1/blockchain")
@RequiredArgsConstructor
@Tag(name = "Blockchain", description = "Методы для работы с блокчейном")
public class BlockchainController {
    private final BlockchainRepository blockchainRepository;
    private final BlockchainService blockchainService;

    @GetMapping(value = "top", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(tags = "blockchain", summary = "Возвращает хэш последнего блока")
    public String top() {
        var hash = blockchainRepository.getLastBlockHash();

        if (Objects.isNull(hash)) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        return Base64.encodeStr(hash);
    }

    @GetMapping(value = "chain", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(tags = "blockchain", summary = "Возвращает список блоков до указанного хэша блока")
    public List<Block> getBlocks(@RequestParam("hash") String hash) {
        return blockchainService.getBlocks(Base64.decode(hash));
    }

    @GetMapping(value = "all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(tags = "blockchain", summary = "Возвращает список всех хэшей")
    public List<String> all() {
        return blockchainRepository.getAllBlocksHashes().stream().map(Base64::encodeStr).toList();
    }
}
