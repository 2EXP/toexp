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

import io.github.toexp.idempotent.annotation.Idempotent;
import io.github.toexp.idempotent.api.IdempotentContextProvider;
import io.github.toexp.idempotent.context.IdempotentContext;
import io.github.toexp.idempotent.exception.EmptyKeyIdempotentException;
import io.github.toexp.idempotent.parser.IAnnotationParser;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

public class IdempotentContextProviderImpl implements IdempotentContextProvider {
    @Autowired
    IAnnotationParser annotationParser;

    public Object idempotentContext(JoinPoint jp) {
        String className = jp.getTarget().getClass().getName();
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();

        Idempotent annotation = method.getAnnotation(Idempotent.class);
        boolean enableIdempotent = annotationParser.parse(jp, annotation.enable(), Boolean.class);
        if (!enableIdempotent) return null;

        String idempotentKey = annotationParser.parse(jp, annotation.key(), String.class);
        if (idempotentKey == null || idempotentKey.length() == 0) {
            throw new EmptyKeyIdempotentException("Idempotent key cannot be empty.");
        }
        idempotentKey = String.format("%s:%s:%s", className, methodName, idempotentKey);

        IdempotentContext idempotentContext = new IdempotentContext();
        idempotentContext.setKey(idempotentKey);
        idempotentContext.setExpireTime(annotation.expireTime());
        idempotentContext.setTimeUnit(annotation.timeUnit());
        idempotentContext.setMaxRetryTimes(annotation.maxRetryTimes());
        idempotentContext.setFailMessage(annotation.failMessage());
        idempotentContext.setProcessMessage(annotation.processMessage());

        return idempotentContext;
    }
}
