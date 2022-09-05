package io.github.toexp.openfeign.test.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"io.github.toexp.openfeign"})
@EnableFeignClients(basePackages = {"io.github.toexp.openfeign.test"})
public class TestConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestConsumerApplication.class, args);
    }
}
