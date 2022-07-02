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

package com.github.toexp.idempotent.parser;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class AnnotationParser implements IAnnotationParser {
    private static final ExpressionParser PARSER = new SpelExpressionParser();

    private static final LocalVariableTableParameterNameDiscoverer DISCOVERER =
            new LocalVariableTableParameterNameDiscoverer();

    @Override
    public <T> T parse(JoinPoint jp, String expr, Class<T> clazz) {
        Object[] args = jp.getArgs();
        String[] params = DISCOVERER.getParameterNames(((MethodSignature) jp.getSignature()).getMethod());
        StandardEvaluationContext context = new StandardEvaluationContext();

        if (params != null && params.length > 0) {
            for (int len = 0; len < params.length; len++) {
                context.setVariable(params[len], args[len]);
            }
        }

        return PARSER.parseExpression(expr).getValue(context, clazz);
    }
}
