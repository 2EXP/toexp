package io.github.toexp.openfeign.test.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"io.github.toexp.openfeign.test.api"})
public class TestConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestConsumerApplication.class, args);
    }
}
