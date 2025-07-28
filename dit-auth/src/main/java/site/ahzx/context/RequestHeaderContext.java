package site.ahzx.context;

import java.util.Map;

public class RequestHeaderContext {

    private static final ThreadLocal<Map<String, String>> context = new ThreadLocal<>();

    public static void setHeaders(Map<String, String> headers) {
        context.set(headers);
    }

    public static Map<String, String> getHeaders() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }
}
