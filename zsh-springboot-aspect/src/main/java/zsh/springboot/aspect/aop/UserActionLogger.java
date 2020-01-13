package zsh.springboot.aspect.aop;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
@Documented
public @interface UserActionLogger {

    String module() default "";     // 打印模块

    String param() default "";      // 打印参数

    String type() default "";

    String paramType() default "type";

}
