package io.github.toexp.openfeign.test.provider;

import io.github.toexp.openfeign.test.api.TestProviderService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestProviderServiceImpl implements TestProviderService {
    @Override
    public String Hello() {
        return "Hello World!";
    }
}
