package zsh.springboot.aloha;

import org.springframework.stereotype.Component;
import zsh.springboot.importbean.ZshBean;
import zsh.springboot.importbean.ZshBeanParameter;

@ZshBean
public interface ZshBeanInterface {

    @ZshBeanParameter
    void print(String print);
}
