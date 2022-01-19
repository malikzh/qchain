package io.github.malikzh.qchain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QChainApplication {

    public static void main(String[] args) {
        SpringApplication.run(QChainApplication.class, args);
    }

}
