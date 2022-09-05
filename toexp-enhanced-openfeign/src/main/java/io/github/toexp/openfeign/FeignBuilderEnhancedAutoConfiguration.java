package io.github.toexp.openfeign;

import feign.MethodMetadata;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.*;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

@Configuration
@ConditionalOnClass(FeignClientSpecification.class)
public class FeignBuilderEnhancedAutoConfiguration {
    @Bean
    public FeignBuilderCustomizer feignBuilderCustomizer() {
        return builder -> builder.contract(new SpringMvcContract() {
            @Override
            protected void processAnnotationOnClass(MethodMetadata data, Class<?> clz) {
                RequestMapping classAnnotation = AnnotatedElementUtils.findMergedAnnotation(clz, RequestMapping.class);
                if (classAnnotation == null) {
                    super.processAnnotationOnClass(data, clz);
                    return;
                }

                if (classAnnotation.path().length == 0) return;
                Assert.state(classAnnotation.path().length == 1, "RequestMapping#path only supports one path");

                String pathPrefix = classAnnotation.path()[0];
                if (StringUtils.hasText(pathPrefix)) {
                    data.template().uri(pathPrefix);
                }
            }
        });
    }
}
