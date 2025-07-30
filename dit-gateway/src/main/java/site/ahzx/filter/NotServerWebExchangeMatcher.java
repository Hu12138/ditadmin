package site.ahzx.filter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class NotServerWebExchangeMatcher implements ServerWebExchangeMatcher {

    private final ServerWebExchangeMatcher delegate;

    public NotServerWebExchangeMatcher(ServerWebExchangeMatcher delegate) {
        this.delegate = delegate;
    }

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        return delegate.matches(exchange)
                .flatMap(result -> result.isMatch() ? MatchResult.notMatch() : MatchResult.match());
    }
}