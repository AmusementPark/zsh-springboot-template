package zsh.springboot.statemachine;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

/**
 * 假设在一个业务系统中，有这样一个对象，它有三个状态：草稿、待发布、发布完成，
 * 针对这三个状态的业务动作也比较简单，分别是：上线、发布、回滚。
 */
@Configuration
@EnableStateMachine
public class StateMachineConfiguration extends EnumStateMachineConfigurerAdapter<StateEnum, EventEnum> {

    /**
     * 状态机初始化配置
     * @param states
     * @throws Exception
     */
    @Override
    public void configure(StateMachineStateConfigurer<StateEnum, EventEnum> states) throws Exception {
        states.withStates().initial(StateEnum.DRAFT).states(EnumSet.allOf(StateEnum.class));
    }

    /**
     * 状态机流转配置
     * @param transitions`
     * @throws Exception
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<StateEnum, EventEnum> transitions) throws Exception {
        transitions
            .withExternal()
            // 上线动作，草稿状态 -> 待发布状态
            .source(StateEnum.DRAFT).target(StateEnum.PUBLISH_TODO).event(EventEnum.ONLINE)
            .and().withExternal()
            // 发布动作 待发布状态 -> 发布状态
            .source(StateEnum.PUBLISH_TODO).target(StateEnum.PUBLISH_DONE).event(EventEnum.PUBLISH)
            .and().withExternal()
            // 撤回动作 发布状态 -> 草稿状态
            .source(StateEnum.PUBLISH_DONE).target(StateEnum.DRAFT).event(EventEnum.ROLLBACK);
    }
}
