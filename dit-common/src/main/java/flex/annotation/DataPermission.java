package flex.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataPermission {
    boolean enabled() default true;
    String userField() default "create_by";
    String deptField() default "dept_id";
}