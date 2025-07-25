package site.ahzx.flex.filter;

import lombok.extern.slf4j.Slf4j;
import site.ahzx.flex.model.LoginUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import site.ahzx.flex.context.LoginContext;

import java.io.IOException;

@Slf4j
@Order(1)
@Component
public class LoginContextFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String tenantIdStr = request.getHeader("x-tenant-id");
            if (StringUtils.hasText(tenantIdStr)) {
                LoginUser user = new LoginUser();
                user.setTenantId(Long.valueOf(tenantIdStr));

                // 其他字段可选填充
                String userIdStr = request.getHeader("x-user-id");
                if (StringUtils.hasText(userIdStr)) {
                    user.setUserId(Long.valueOf(userIdStr));
                }

                String username = request.getHeader("x-username");
                if (StringUtils.hasText(username)) {
                    user.setUsername(username);
                }

                String deptIdStr = request.getHeader("x-dept-id");
                if (StringUtils.hasText(deptIdStr)) {
                    user.setDeptId(Long.valueOf(deptIdStr));
                }

                // 自定义字段也一样处理……
                LoginContext.set(user);
                log.info("LoginContextFilter: {}", user);
            }
            filterChain.doFilter(request, response);
        } finally {
            LoginContext.clear();  // 防止线程复用时脏数据
        }
    }
}