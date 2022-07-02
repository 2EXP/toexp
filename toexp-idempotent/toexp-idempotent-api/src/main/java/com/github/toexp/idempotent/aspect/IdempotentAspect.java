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

package com.github.toexp.idempotent.aspect;

import com.github.toexp.idempotent.api.IIdempotentContextProvider;
import com.github.toexp.idempotent.api.IIdempotentProvider;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class IdempotentAspect {
    IIdempotentContextProvider idempotentKeyProvider;

    IIdempotentProvider idempotentProvider;

    public IdempotentAspect(IIdempotentContextProvider keyProvider,
                            IIdempotentProvider provider) {
        idempotentKeyProvider = keyProvider;
        idempotentProvider = provider;
    }

    @Around("@annotation(com.github.toexp.idempotent.annotation.Idempotent)")
    public Object execute(ProceedingJoinPoint jp) throws Throwable {
        Object idempotentContext = idempotentKeyProvider.idempotentContext(jp);
        if (idempotentContext == null) return jp.proceed();

        try {
            Object cacheResult = idempotentProvider.checkAndGetIdempotent(idempotentContext);
            if (cacheResult != null) return cacheResult;

            Object result = jp.proceed();
            idempotentProvider.handleIdempotentSuccess(idempotentContext, result);
            return result;
        } catch (Throwable e) {
            return idempotentProvider.handleIdempotentException(idempotentContext, e);
        } finally {
            idempotentProvider.handleIdempotentFinally(idempotentContext);
        }
    }
}
