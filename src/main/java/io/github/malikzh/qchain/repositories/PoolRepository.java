package io.github.malikzh.qchain.repositories;

import io.github.malikzh.qchain.configurations.QChainConfiguration;
import org.springframework.stereotype.Repository;

@Repository
public class PoolRepository extends LevelDbRepository{
    public PoolRepository(QChainConfiguration properties) {
        super(properties);
    }

    @Override
    public String getDbName() {
        return "pool_db";
    }
}
