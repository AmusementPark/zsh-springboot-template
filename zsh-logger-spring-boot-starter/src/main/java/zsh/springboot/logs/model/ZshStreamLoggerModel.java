package zsh.springboot.logs.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Data
@Setter
@Getter
@ApiModel(value = "RosLogsRecordModel对象", description = "")
public class ZshStreamLoggerModel {

    private Long id;

    @ApiModelProperty(value = "操作者账号")
    private String account;

    @ApiModelProperty(value = "操作模块")
    private String module;

    @ApiModelProperty(value = "操作方法")
    private String method;

    @ApiModelProperty(value = "方法类型 post get  delete")
    private String methodType;

    @ApiModelProperty(value = "操作请求的ip地址")
    private String ip;

    @ApiModelProperty(value = "操作请求的类地址")
    private String classUrl;

    @ApiModelProperty(value = "操作请求的url地址")
    private String url;

    @ApiModelProperty(value = "新增MAid不等于主键ID")
    private String requestParam;

    @ApiModelProperty(value = "返回参数")
    private String responseParam;

    @ApiModelProperty(value = "方法说明参数")
    private String record;

    @ApiModelProperty(value = "方法说明参数")
    private String enRecord;

    @ApiModelProperty(value = "1:rvdc 2:ota")
    private Integer logType;

    @ApiModelProperty(value = "操作时间")
    private Date operationDate;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
