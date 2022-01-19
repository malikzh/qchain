package io.github.malikzh.qchain.repositories;

import io.github.malikzh.qchain.configurations.QChainConfiguration;
import lombok.SneakyThrows;
import org.iq80.leveldb.WriteBatch;
import org.springframework.stereotype.Repository;

import static org.iq80.leveldb.impl.Iq80DBFactory.asString;
import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;

@Repository
public class PoolRepository extends LevelDbRepository{
    public PoolRepository(QChainConfiguration properties) {
        super(properties);
    }

    @Override
    public String getDbName() {
        return "pool_db";
    }

    @SneakyThrows
    public void save(byte[] key, String xml) {
        try (WriteBatch batch = db.createWriteBatch()) {
            db.put(key, bytes(xml));
            setSize(batch, getSize() + 1);

            db.write(batch);
        }
    }

    public String find(byte[] key) {
        return asString(db.get(key));
    }
}
