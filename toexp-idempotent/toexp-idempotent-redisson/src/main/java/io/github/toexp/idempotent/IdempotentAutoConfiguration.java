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

package io.github.toexp.idempotent;

import io.github.toexp.idempotent.api.IdempotentContextProvider;
import io.github.toexp.idempotent.api.IdempotentProvider;
import io.github.toexp.idempotent.aspect.IdempotentAspect;
import io.github.toexp.idempotent.parser.AnnotationParser;
import io.github.toexp.idempotent.parser.IAnnotationParser;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class IdempotentAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(IdempotentContextProvider.class)
    public IdempotentContextProvider idempotentContextProvider() { return new IdempotentContextProviderImpl(); }

    @Bean
    @ConditionalOnMissingBean(IdempotentProvider.class)
    public IdempotentProvider idempotentProvider() { return new IdempotentProviderImpl(); }

    @Bean
    @ConditionalOnMissingBean(IAnnotationParser.class)
    public IAnnotationParser annotationParser() { return new AnnotationParser(); }

    @Bean
    public IdempotentAspect idempotentAspect(IdempotentContextProvider idempotentContextProvider,
                                             IdempotentProvider idempotentProvider) {
        return new IdempotentAspect(idempotentContextProvider, idempotentProvider);
    }
}
