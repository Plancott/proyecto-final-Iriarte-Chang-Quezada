package org.example.apigatewayms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayMsApplication.class, args);
    }

}
