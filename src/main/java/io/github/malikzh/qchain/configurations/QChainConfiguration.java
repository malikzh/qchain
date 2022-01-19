package io.github.malikzh.qchain.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("qchain")
@Data
public class QChainConfiguration {
    // Путь к каталогу с данными
    private String dataPath;

    // Максимальный размер пула транзакций (Если < 1, то бесконечно)
    private Integer maxPoolSize;
}
