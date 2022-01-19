package io.github.malikzh.qchain.repositories;

import io.github.malikzh.qchain.configurations.QChainConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.WriteBatch;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.nio.file.Paths;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;

@RequiredArgsConstructor
@Slf4j
public abstract class LevelDbRepository {
    abstract public String getDbName();

    protected DB db;
    private final QChainConfiguration properties;

    protected final static String DB_SIZE_KEY = "__db_size__";

    @PostConstruct
    @SneakyThrows
    private void initialize() {
        Options options = new Options();
        options.createIfMissing(true);

        // db path
        String path = Paths.get(System.getProperty("user.dir"), properties.getDataPath(), getDbName()).toString();

        log.info("Creating '{}' database at: {}", getDbName(), path);
        db = factory.open(new File(path), options);
        db.put(bytes(DB_SIZE_KEY), bytes("0"));
    }

    protected void setSize(WriteBatch batch, Integer size) {
        batch.put(bytes(DB_SIZE_KEY), bytes(size.toString()));
    }

    public Integer getSize() {
        return Integer.valueOf(asString(db.get(bytes(DB_SIZE_KEY))));
    }

    @PreDestroy
    @SneakyThrows
    private void destroy() {
        log.info("Closing '{}' database...", getDbName());
        db.close();
    }
}
