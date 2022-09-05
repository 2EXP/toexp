package io.github.toexp.openfeign.test.consumer;

import io.github.toexp.openfeign.test.api.TestProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consumer")
public class TestConsumerController {
    @Autowired
    private TestProviderService testProviderService;

    @GetMapping("/hello")
    public String Hello() {
        return "Consumer: " + testProviderService.Hello();
    }
}
