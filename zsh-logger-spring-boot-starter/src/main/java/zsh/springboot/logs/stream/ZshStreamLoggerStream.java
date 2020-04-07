package zsh.springboot.logs.stream;

import zsh.springboot.logs.model.ZshStreamLoggerModel;

/**
 * @author Shenghao.Zhu
 */
public interface ZshStreamLoggerStream {
    void sendRosWebLog(ZshStreamLoggerModel model);
}
