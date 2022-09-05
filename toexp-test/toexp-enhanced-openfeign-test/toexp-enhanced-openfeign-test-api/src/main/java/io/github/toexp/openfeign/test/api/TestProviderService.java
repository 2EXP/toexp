package io.github.toexp.openfeign.test.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "test-provider", url = "http://localhost:28080")
@RequestMapping("/provider")
public interface TestProviderService {
    @GetMapping("/hello")
    String Hello();
}
