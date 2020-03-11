package zsh.springboot.reactiveweb;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
@Documented
public @interface UserActionLogger {
    String module() default "";     // 打印模块
    Class<? extends UserSessionFetchAdapter> userSessionFetchAdapter() default NoUserSessionFetchAdapter.class;
}
