package io.github.malikzh.qchain.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("qchain")
@Data
public class QChainConfiguration {
    private String dataPath;
}
