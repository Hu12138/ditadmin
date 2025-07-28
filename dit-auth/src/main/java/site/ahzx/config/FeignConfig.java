package site.ahzx.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.ahzx.feign.DynamicFeignHeaderInterceptor;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor dynamicFeignHeaderInterceptor() {
        return new DynamicFeignHeaderInterceptor();
    }
}
