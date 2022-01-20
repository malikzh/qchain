package io.github.malikzh.qchain.repositories;

import io.github.malikzh.qchain.configurations.QChainConfiguration;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Objects;

import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;

@Repository
public class BlockchainRepository extends LevelDbRepository {
    private static final String LAST_BLOCK_FIELD = "__last";

    public BlockchainRepository(QChainConfiguration properties) {
        super(properties);
    }

    @Override
    public String getDbName() {
        return "blockchain_db";
    }


    /**
     * Возвращает хэш последнего блока
     * @return Хэш последнего блока, если последнего блока нет, то null
     */
    public @Nullable byte[] getLastBlockHash() {
        return db.get(bytes(LAST_BLOCK_FIELD));
    }

    /**
     * Возвращает глубину до блока
     * @param blockHash Хэш блока, до которого надо дойти
     * @return Глубина до блока, если блок не найден с соответствующим хэшем, то возвращается -1
     */
    public @NonNull Integer getDepthTo(byte[] blockHash) {
        var last = getLastBlockHash();

        if (Objects.isNull(last)) {
            return -1;
        }

        if (Arrays.equals(blockHash, last)) {
            return 0;
        }

        // todo
        return 0;
    }

    @Override
    public void save(byte[] key, byte[] value) {
        var batch = db.createWriteBatch();

        batch.put(bytes(LAST_BLOCK_FIELD), key);

        save(key, value, batch);
    }

    @Override
    public void save(byte[] key, String value) {
        var batch = db.createWriteBatch();

        batch.put(bytes(LAST_BLOCK_FIELD), key);

        save(key, bytes(value), batch);
    }
}
