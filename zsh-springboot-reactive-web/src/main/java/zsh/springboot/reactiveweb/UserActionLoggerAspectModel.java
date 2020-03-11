package zsh.springboot.reactiveweb;

import lombok.Data;

import java.util.Date;

@Data
public class UserActionLoggerAspectModel {

    //操作者账号
    private String operator;

    //操作模块
    private String module;

    //操作方法
    private String method;

    //方法类型 POST/PUT/DELETE
    private String methodType;

    //操作请求的ip地址
    private String ip;

    //操作请求的类地址
    private String clazz;

    //操作请求的uri
    private String uri;

    //请求参数
    private String reqParamJson;

    //返回参数
    private String resParamJson;

    //操作时间
    private Date operationDate;
}
