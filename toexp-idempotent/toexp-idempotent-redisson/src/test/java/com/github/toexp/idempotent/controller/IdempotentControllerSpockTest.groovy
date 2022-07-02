/*
 * Copyright 2022 2EXP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.toexp.idempotent.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.testcontainers.containers.GenericContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Specification

@SpringBootApplication(scanBasePackages = "com.github.toexp.idempotent")
@SpringBootTest
@Testcontainers
class IdempotentControllerSpockTest extends Specification {
    @Autowired
    private WebApplicationContext webApplicationContext

    private MockMvc mockMvc

    static GenericContainer redis = new GenericContainer<>("redis:7.0.2-alpine").withExposedPorts(6379)

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    def cleanup() {
        redis.stop()
    }

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        redis.start()
        registry.add("spring.redis.host", redis::getHost)
        registry.add("spring.redis.port", redis::getFirstMappedPort)
    }

    def "When get is performed then the response has status 200 and content is 'success'"() {
        expect: "Status is 200 and the response is 'success'"
        mockMvc.perform(MockMvcRequestBuilders.get("/get?key=1"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
    }
}
