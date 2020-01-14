package zsh.springboot.aloha.swagger;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("swagger")
@Api(tags="测试接口API")
public class SwaggerController {

    /** 登录
     * @return
     */
    @ApiOperation(value="登录", notes="登录接口")
    @PostMapping(value="/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginVo loginVo) {
        return ResponseEntity.ok(String.format("SUCCESS: %s, %s", loginVo.getUsername(), loginVo.getPassword()));
    }
}
