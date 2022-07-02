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

package com.github.toexp.idempotent.dataobject;

import com.github.toexp.idempotent.enums.IdempotentStateEnum;

public class IdempotentDo {
    private IdempotentStateEnum state = IdempotentStateEnum.SUCCESS;

    private Integer retryTimes = 0;

    private Object result = null;

    public IdempotentStateEnum getState() {
        return state;
    }

    public void setState(IdempotentStateEnum state) {
        this.state = state;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
