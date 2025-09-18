package org.example.stockms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class StockMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockMsApplication.class, args);
    }

}
