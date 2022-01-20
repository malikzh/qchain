package io.github.malikzh.qchain.repositories;

import io.github.malikzh.qchain.configurations.QChainConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.WriteBatch;
import org.springframework.lang.NonNull;

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

        log.info("Opening '{}' database at: {}", getDbName(), path);
        db = factory.open(new File(path), options);

        if (db.get(bytes(DB_SIZE_KEY)) == null) {
            log.info("Initializing '{}' database", getDbName());
            db.put(bytes(DB_SIZE_KEY), bytes("0"));
        }
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

    @SneakyThrows
    public void save(byte[] key, String value) {
        save(key, bytes(value), db.createWriteBatch());
    }

    @SneakyThrows
    public void save(byte[] key, byte[] value) {
        save(key, value, db.createWriteBatch());
    }

    @SneakyThrows
    protected void save(byte[] key, byte[] value, @NonNull WriteBatch batch) {
        try (batch) {
            batch.put(key, value);

            if (db.get(key) == null) {
                setSize(batch, getSize() + 1);
            }

            db.write(batch);
        }
    }

    public String find(byte[] key) {
        return asString(db.get(key));
    }

    @SneakyThrows
    public void delete(byte[] key) {
        try (WriteBatch batch = db.createWriteBatch()) {
            if (db.get(key) != null) {
                setSize(batch, getSize() - 1);
            }

            batch.delete(key);

            db.write(batch);
        }
    }
}
