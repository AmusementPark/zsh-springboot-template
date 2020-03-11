package zsh.springboot.reactiveweb.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class LoginVo implements Serializable {

    @NotBlank(message = "ERR_USERNAME_BLANK")
    @Length(min=4,max=8)
    private String username;

    @NotBlank(message = "ERR_PASSWORD_BLANK")
    @Length(min=4,max=8)
    private String password;
}
