package io.github.malikzh.qchain.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malikzh.qchain.configurations.QChainConfiguration;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Set;

import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;

/**
 * Репозиторий для хранения всяких состояний
 */

@Repository
public class StateRepository extends LevelDbRepository {
    public final static String PEERS_KEY = "__peers";

    public StateRepository(QChainConfiguration properties) {
        super(properties);
    }

    @Override
    public String getDbName() {
        return "state_db";
    }

    @SneakyThrows
    public Set<String> getPeers() {
        var om = new ObjectMapper();
        var raw = find(bytes(PEERS_KEY));

        if (Objects.isNull(raw)) {
            return null;
        }

        return om.readValue(raw, Set.class);
    }

    @SneakyThrows
    public void setPeers(Set<String> peers) {
        var om = new ObjectMapper();
        save(bytes(PEERS_KEY), om.writeValueAsBytes(peers));
    }
}
