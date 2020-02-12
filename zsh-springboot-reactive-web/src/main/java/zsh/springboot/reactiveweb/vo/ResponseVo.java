package zsh.springboot.reactiveweb.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ResponseVo<T> {
    private String code;
    private String message;
    private T data;

    public static ResponseVo<Void> ok() {
        return ResponseVo.<Void>builder().code("0").message("SUCCESS").data(null).build();
    }

    public static ResponseVo<Void> erro(String message) {
        return ResponseVo.<Void>builder().code("-1").message(message).data(null).build();
    }
}
