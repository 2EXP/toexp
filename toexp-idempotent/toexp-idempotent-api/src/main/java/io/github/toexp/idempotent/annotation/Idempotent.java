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

package io.github.toexp.idempotent.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Idempotent annotation
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
    /**
     * Unique identifier
     *
     * @return Spring-EL expression
     */
    String key() default "";

    /**
     * Idempotent switch
     *
     * @return Spring-EL expression
     */
    String enable() default "true";

    /**
     * Idempotent expire time, default: 1s
     *
     * @return Expire time
     */
    int expireTime() default 1;

    /**
     * Time Unit, default: second
     *
     * @return Time unit
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * Maximum available method retry times, default: 3
     *
     * @return Maximum retry times
     */
    int maxRetryTimes() default 3;

    /**
     * Idempotent fail exception message, default: ""
     *
     * @return Fail message
     */
    String failMessage() default "";

    /**
     * Idempotent processing exception message, default ""
     *
     * @return Processing message
     */
    String processMessage() default "";

    /**
     * Whether to delete idempotent after the intercepted method executed, default: false
     *
     * @return If true, delete idempotent after method executed
     */
    String finalDelete() default "false";
}
