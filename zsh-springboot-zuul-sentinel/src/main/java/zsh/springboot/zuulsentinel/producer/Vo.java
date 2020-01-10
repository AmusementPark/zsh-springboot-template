package zsh.springboot.zuulsentinel.producer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Vo<T> {
    String code;
    String message;
    T data;
}
