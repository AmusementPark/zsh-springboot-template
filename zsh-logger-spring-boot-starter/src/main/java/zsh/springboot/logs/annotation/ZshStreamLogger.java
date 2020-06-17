package zsh.springboot.logs.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: RosWebLog
 * @Description:TODO Intercept operation log
 * @Author: pengwei.wang
 * @Date: 2019/9/17 17:06
 * @Version V1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ZshStreamLogger {

    /**
     * 中文版接口描述
     */
    String description() default "";

    /**
     * 英文版接口描述
     */
    String enDescription() default "";

    /**
     * 对应页面的具体操作的模块
     */
    String module() default "";

    /**
     * 入参的值
     */
    String param() default "";

    /**
     * 具体type类型：1：rvdc  2：ota;
     * 优先级大于 paramType
     */
    String type() default "";

    /**
     * 从入参中获取type类型：1：rvdc  2：ota;
     * 优先级小于type
     */
    String paramType() default "type";

}
