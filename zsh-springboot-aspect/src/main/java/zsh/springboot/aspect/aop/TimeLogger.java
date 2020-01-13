package zsh.springboot.aspect.aop;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TimeLogger {
    String   logTopic() default "";
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
