package zsh.springboot.aloha.swagger;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class LoginVo implements Serializable {

    @NotBlank(message = "ERR_USERNAME_BLANK")
    @Length(min=4,max=8)
    @ApiParam(name="username", value="账号")
    private String username;

    @NotBlank(message = "ERR_PASSWORD_BLANK")
    @Length(min=4,max=8)
    @ApiParam(name="password", value="密码")
    private String password;
}
