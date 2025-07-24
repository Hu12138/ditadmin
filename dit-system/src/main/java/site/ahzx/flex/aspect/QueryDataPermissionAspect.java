package site.ahzx.flex.aspect;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.query.QueryColumn;
import site.ahzx.flex.annotation.QueryDataPermission;
import site.ahzx.flex.enums.DataScope;
import site.ahzx.flex.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import site.ahzx.flex.context.LoginContext;

import java.lang.reflect.Method;
import java.util.Set;

@Slf4j
@Aspect
@Component
public class QueryDataPermissionAspect {

    @Around("@annotation(site.ahzx.flex.annotation.QueryDataPermission)")
    public Object injectDataPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        QueryDataPermission permission = method.getAnnotation(QueryDataPermission.class);
        if (!permission.enabled()) {
            return joinPoint.proceed();
        }

        Object[] args = joinPoint.getArgs();
        QueryWrapper wrapper = findQueryWrapperArg(args);
        if (wrapper == null) {
            return joinPoint.proceed();
        }

        LoginUser loginUser = LoginContext.get();
        if (loginUser == null) {
            return joinPoint.proceed();
        }

        // 获取字段名
        String deptField = permission.deptField();
        String userField = permission.userField();

        QueryColumn deptColumn = new QueryColumn(deptField);
        QueryColumn userColumn = new QueryColumn(userField);

        // 权限控制逻辑
        DataScope scope = DataScope.from(loginUser.getDataScope());
        switch (scope) {
            case ALL:
                // 无权限限制，不加任何条件
                break;
            case SELF:
                wrapper.and(userColumn.eq(loginUser.getUserId()));
                break;
            case DEPT_ONLY:
                wrapper.and(deptColumn.eq(loginUser.getDeptId()));
                break;
            case DEPT_AND_CHILD: {
                Set<Long> deptIds = loginUser.getCustomDeptIds();

                wrapper.and(deptColumn.in(deptIds));

                break;
            }
            case CUSTOM: {
                Set<Long> customDepts = loginUser.getCustomDeptIds();
                wrapper.and(deptColumn.in(customDepts));

            }
            default:
                log.warn("Unknown data scope: {}", scope);
                break;
        }

        return joinPoint.proceed();
    }

    private QueryWrapper findQueryWrapperArg(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof QueryWrapper) {
                return (QueryWrapper) arg;
            }
        }
        return null;
    }
}