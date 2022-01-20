package io.github.malikzh.qchain.services;

import io.github.malikzh.qchain.configurations.QChainConfiguration;
import io.github.malikzh.qchain.repositories.StateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PeerService {
    private final StateRepository state;
    private final QChainConfiguration config;

    @PostConstruct
    private void initialize() {
        if (Objects.nonNull(state.getPeers())) {
            return;
        }

        log.info("Creating peers table...");

        List<String> peers = new ArrayList<String>();

        if (config.getPeers() != null ) {
            peers = config.getPeers();
        }

        log.info("Added {} to peers table.", peers);

        state.setPeers(peers);
    }
}
