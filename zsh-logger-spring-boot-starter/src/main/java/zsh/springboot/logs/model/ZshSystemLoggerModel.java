package zsh.springboot.logs.model;


import lombok.Data;

import java.io.Serializable;

@Data
public class ZshSystemLoggerModel implements Serializable {

    private boolean async;

    private String entryPoint;
    private String location;
    private String description;
    private String className;
    private String methodName;
    private String arguments;
    private String result;
    private Long elapsedTime;
    private String monitor;

    public String format() {
        return "调用接口: [{}], 花费时间: [{}], 注解位置: [{}], 方法描述: [{}], 目标类名: [{}], 目标方法: [{}], 调用参数: [{}], 返回结果: [{}]";
    }

    public Object[] args() {
        return new Object[]{this.entryPoint, this.elapsedTime, this.location, this.description, this.className, this.methodName, this.arguments, this.result};
    }

}
