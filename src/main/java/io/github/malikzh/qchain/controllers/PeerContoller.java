package io.github.malikzh.qchain.controllers;

import io.github.malikzh.qchain.services.PeerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
@RequestMapping(path = "qchain/api/v1/peers")
@RequiredArgsConstructor
@Tag(name = "Peer", description = "Методы для работы с синхронизацией списков пиров")
public class PeerContoller {
    private final PeerService peerService;

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(tags = "transaction", summary = "Обмен пирами")
    public ResponseEntity<Set<String>> exchange(@RequestBody Set<String> peers) {
        if (peers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Список пиров пуст");
        }

        peerService.add(peers);

        return new ResponseEntity<>(peerService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(tags = "transaction", summary = "Возвращает список пиров")
    public Set<String> getAll() {
        return peerService.getAll();
    }
}
