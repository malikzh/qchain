package io.github.malikzh.qchain.services;

import io.github.malikzh.qchain.configurations.QChainConfiguration;
import io.github.malikzh.qchain.repositories.StateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PeerService {
    private final StateRepository state;
    private final QChainConfiguration config;

    /**
     * Регистрация новых пиров
     * @param peers
     */
    public void add(Set<String> peers) {
        Set<String> current = config.getPeers();

        current.addAll(peers.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet()));

        state.setPeers(current);
    }

    /**
     * Возвращает множество всех пиров
     *
     * @return
     */
    public Set<String> getAll() {
        return state.getPeers();
    }

    /**
     * Инициализация таблицы пиров
     */
    @PostConstruct
    private void initialize() {
        if (Objects.nonNull(state.getPeers())) {
            return;
        }

        log.info("Creating peers table...");

        Set<String> peers = new HashSet<>();

        if (config.getPeers() != null ) {
            peers = config.getPeers();
        }

        peers = peers.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        state.setPeers(peers);


        log.info("Added {} to peers table.", peers);
    }
}
