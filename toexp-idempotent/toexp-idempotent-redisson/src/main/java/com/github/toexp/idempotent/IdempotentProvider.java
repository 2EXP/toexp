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

package com.github.toexp.idempotent;

import com.github.toexp.idempotent.api.IIdempotentProvider;
import com.github.toexp.idempotent.context.IdempotentContext;
import com.github.toexp.idempotent.dataobject.IdempotentDo;
import com.github.toexp.idempotent.enums.IdempotentStateEnum;
import com.github.toexp.idempotent.exception.BypassUpdateIdempotentException;
import com.github.toexp.idempotent.exception.MaxRetryIdempotentException;
import com.github.toexp.idempotent.exception.ProcessingIdempotentException;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.codec.AvroJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class IdempotentProvider implements IIdempotentProvider {
    private static final String CACHE_KEY = "toexp:idempotent";

    private static final ThreadLocal<IdempotentDo> IDEMPOTENT_THREAD_LOCAL = ThreadLocal.withInitial(IdempotentDo::new);

    @Autowired
    private RedissonClient redisson;

    private RMapCache<String, IdempotentDo> rMapCache;

    @PostConstruct
    private void init() {
        rMapCache = redisson.getMapCache(CACHE_KEY, AvroJacksonCodec.INSTANCE);
    }

    @Override
    public Object checkAndGetIdempotent(Object aContext) {
        IdempotentContext context = (IdempotentContext) aContext;
        IdempotentDo newDo = new IdempotentDo();
        newDo.setState(IdempotentStateEnum.PROCESSING);
        IdempotentDo oldDo = rMapCache.putIfAbsent(context.key(), newDo, context.expireTime(),
                context.timeUnit());

        if (oldDo == null) return null;
        if (IdempotentStateEnum.SUCCESS.equals(oldDo.getState())) return oldDo.getResult();
        if (IdempotentStateEnum.PROCESSING.equals(oldDo.getState())) {
            throw new ProcessingIdempotentException(context.processMessage());
        }
        if (IdempotentStateEnum.FAIL.equals(oldDo.getState()) && oldDo.getRetryTimes() > context.maxRetryTimes()) {
            throw new MaxRetryIdempotentException(context.failMessage());
        }

        oldDo.setState(IdempotentStateEnum.PROCESSING);
        oldDo.setRetryTimes(oldDo.getRetryTimes() + 1);
        rMapCache.put(context.key(), oldDo, context.expireTime(), context.timeUnit());
        IDEMPOTENT_THREAD_LOCAL.set(oldDo);

        return null;
    }

    @Override
    public void handleIdempotentSuccess(Object aContext, Object result) {
        IdempotentContext context = (IdempotentContext) aContext;
        IdempotentDo idempotentDo = new IdempotentDo();
        idempotentDo.setState(IdempotentStateEnum.SUCCESS);
        idempotentDo.setResult(result);
        rMapCache.put(context.key(), idempotentDo);
    }

    @Override
    public Object handleIdempotentException(Object aContext, Throwable e) throws Throwable {
        if (e instanceof BypassUpdateIdempotentException) {
            throw e;
        }

        IdempotentContext context = (IdempotentContext) aContext;
        IdempotentDo idempotentDo = IDEMPOTENT_THREAD_LOCAL.get();
        idempotentDo.setState(IdempotentStateEnum.FAIL);
        idempotentDo.setResult(e);
        rMapCache.put(context.key(), idempotentDo);
        throw e;
    }

    @Override
    public void handleIdempotentFinally(Object aContext) {
        IdempotentContext context = (IdempotentContext) aContext;
        if (context.finalDelete()) rMapCache.fastRemove(context.key());
    }
}
