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

package com.github.toexp.idempotent.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.spock.Testcontainers;

import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
public class IdempotentControllerTest {
    static GenericContainer redis = new GenericContainer<>("redis:7.0.2-alpine").withExposedPorts(6379);

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        redis.start();
        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", redis::getFirstMappedPort);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * Get with key in one request
     */
    @Test
    void getWithKeySingleTest() throws Exception {
        mockMvc.perform(get("/get?key=1")).andExpect(status().isOk()).andReturn();
    }

    /**
     * Get with key in 5 concurrent requests
     */
    @RepeatedTest(5)
    @Execution(CONCURRENT)
    void getWithKeyRepeatTest() {
        try {
            mockMvc.perform(get("/get?key=2")).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get with key in 5 concurrent requests
     */
    @RepeatedTest(5)
    @Execution(CONCURRENT)
    void getWithSwitchRepeatTest() {
        try {
            mockMvc.perform(get("/getWithSwitch?key=3&enable=false")).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get without key in 5 concurrent requests
     */
    @RepeatedTest(5)
    @Execution(CONCURRENT)
    void getWithoutKeyRepeatTest() {
        try {
            mockMvc.perform(get("/getWithoutKey")).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
