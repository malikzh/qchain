package io.github.malikzh.qchain.repositories;

import io.github.malikzh.qchain.configurations.QChainConfiguration;
import lombok.SneakyThrows;
import org.iq80.leveldb.DBIterator;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PoolRepository extends LevelDbRepository{
    public PoolRepository(QChainConfiguration properties) {
        super(properties);
    }

    @Override
    public String getDbName() {
        return "pool_db";
    }

    /**
     * Возвращает список транзакций из БД
     *
     * @param max
     * @return
     */
    @SneakyThrows
    public List<byte[]> get(Integer max) {
        var result = new ArrayList<byte[]>();
        var counter = 0;

        try (DBIterator it = db.iterator()) {
            for(it.seekToFirst(); it.hasNext(); it.next()) {
                if (it.peekNext().getKey().length != 32) continue;
                if (max > 0 && counter++ >= max) break;

                result.add(it.peekNext().getValue());
            }
        }

        return result;
    }
}
