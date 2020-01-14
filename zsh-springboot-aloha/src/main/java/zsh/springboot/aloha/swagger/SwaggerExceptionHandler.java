package zsh.springboot.aloha.swagger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class SwaggerExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseVo handleBindException(MethodArgumentNotValidException e) {
        // ex.getFieldError():随机返回一个对象属性的异常信息。如果要一次性返回所有对象属性异常信息，则调用ex.getAllErrors()
        FieldError fieldError = e.getBindingResult().getFieldError();
        StringBuilder sb = new StringBuilder();
        sb.append(fieldError.getField()).append("=[").append(fieldError.getRejectedValue()).append("]").append(fieldError.getDefaultMessage());
        return ResponseVo.erro(sb.toString());
    }
}
