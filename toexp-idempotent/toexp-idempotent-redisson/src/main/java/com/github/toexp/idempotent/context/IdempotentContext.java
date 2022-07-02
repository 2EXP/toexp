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

package com.github.toexp.idempotent.context;

import java.util.concurrent.TimeUnit;

public class IdempotentContext {
    private String key;
    private int expireTime;
    private TimeUnit timeUnit;
    private int maxRetryTimes;
    private String failMessage;
    private String processMessage;
    private boolean finalDelete;

    public String key() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int expireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public TimeUnit timeUnit() { return timeUnit; }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public int maxRetryTimes() {
        return maxRetryTimes;
    }

    public void setMaxRetryTimes(int maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
    }

    public String failMessage() {
        return failMessage;
    }

    public void setFailMessage(String failMessage) {
        this.failMessage = failMessage;
    }

    public String processMessage() {
        return processMessage;
    }

    public void setProcessMessage(String processMessage) {
        this.processMessage = processMessage;
    }

    public boolean finalDelete() { return finalDelete; }

    public void setFinalDelete(boolean finalDelete) { this.finalDelete = finalDelete; }
}
