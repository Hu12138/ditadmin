package site.ahzx.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import site.ahzx.context.RequestHeaderContext;

import java.util.Map;

public class DynamicFeignHeaderInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        Map<String, String> headers = RequestHeaderContext.getHeaders();
        if (headers != null) {
            headers.forEach(requestTemplate::header);
        }
    }
}
