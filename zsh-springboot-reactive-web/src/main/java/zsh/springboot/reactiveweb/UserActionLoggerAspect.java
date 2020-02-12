package zsh.springboot.reactiveweb;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.adapter.DefaultServerWebExchange;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Slf4j
@Aspect
@Component
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class UserActionLoggerAspect {

    private final String POINT_CUT = "@annotation(zsh.springboot.reactiveweb.UserActionLogger)";
    private ThreadLocal<UserActionLoggerAspectModel> logThreadLocal = new ThreadLocal<>();
    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Before(value=POINT_CUT)
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        if (!matchApiAnnotation(joinPoint)) {
            return;
        }

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        UserSessionFetchAdapter userSessionFetchAdapter = (UserSessionFetchAdapter) applicationContext.getBean(getCandidateOperatorClazz(joinPoint));

        UserActionLoggerAspectModel userActionLoggerAspectModel = new UserActionLoggerAspectModel();
        userActionLoggerAspectModel.setOperator(userSessionFetchAdapter.operator(request));
        userActionLoggerAspectModel.setUri(request.getRequestURI());
        userActionLoggerAspectModel.setIp(request.getRemoteAddr());
        userActionLoggerAspectModel.setClazz(joinPoint.getSignature().getDeclaringTypeName());
        userActionLoggerAspectModel.setMethodType(request.getMethod());
        userActionLoggerAspectModel.setModule(getAspectLogModule(joinPoint));
        userActionLoggerAspectModel.setMethod(joinPoint.getSignature().getName());
        logThreadLocal.set(userActionLoggerAspectModel);
    }

    @Around(value=POINT_CUT)
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        if (!matchApiAnnotation(proceedingJoinPoint)) {
            return proceedingJoinPoint.proceed();
        }

        Object result = proceedingJoinPoint.proceed();                                          // 获取到请求参数
        Object candidateLogParameter = getCandidateLogParameter(proceedingJoinPoint);           // 获取到请求参数
        UserActionLoggerAspectModel userActionLoggerAspectModel = logThreadLocal.get();

        userActionLoggerAspectModel.setReqParamJson(JSON.toJSONString(candidateLogParameter));  // 设置 request param
        userActionLoggerAspectModel.setResParamJson(JSON.toJSONString(result));                 // 设置 response param
        return result;
    }

    @After(value=POINT_CUT)
    public void doAfter() throws Throwable {
        UserActionLoggerAspectModel userActionLoggerAspectModel = logThreadLocal.get();
        System.out.println(userActionLoggerAspectModel);
        if (userActionLoggerAspectModel == null) {
            return;
        }
//        sender.sendMessage("ros-logs-topic",JSON.toJSONString(recordModel));
    }
//
//    @AfterThrowing(value=POINT_CUT, throwing = "e")
//    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
//        handleLog(joinPoint, e, null);
//    }
//
    /**
     * 不匹配@RequestMapping，则不做处理
     * @param proceedingJoinPoint
     * @return
     */
    private boolean matchApiAnnotation(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RequestMapping requestMapping = AnnotationUtils.getAnnotation(method, RequestMapping.class);
        if ( requestMapping == null || requestMapping.method().length !=1 ) return false;
        return !requestMapping.method()[0].equals(RequestMethod.GET);
    }

    private boolean matchApiAnnotation(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RequestMapping requestMapping = AnnotationUtils.getAnnotation(method, RequestMapping.class);
        if ( requestMapping == null || requestMapping.method().length !=1 ) return false;
        return !requestMapping.method()[0].equals(RequestMethod.GET);
    }

//    private void handleLog(JoinPoint joinPoint, Exception e, Object o) {
//        try {
//            if (e != null){
//                RosLogsRecordModel recordModel=threadPerlog.get();
//                String substring = StringUtils.substring(e.getMessage(), 0, 2000);
//                recordModel.setRecord(substring);
//                recordModel.setEnRecord(substring);
//            }
//        } catch (Exception e1) {
//            log.error("==前置通知异常==");
//            log.error("异常信息:{}", e1.getMessage());
//        }
//    }

    public String getAspectLogModule(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        UserActionLogger userActionLogger = method.getAnnotation(UserActionLogger.class);
        return userActionLogger.module();
    }

    /**
     * 查找candidate
     * @param joinPoint
     * @return
     */
    private Object getCandidateLogParameter(ProceedingJoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 只查找@RequestBody并记录
        Class<?> candidate = null;
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(RequestBody.class)) {
                candidate = parameter.getType();
            }
        }
        if (candidate != null) {
            for (Object arg : args) {
                if (arg.getClass().equals(candidate)) {
                    return arg;
                }
            }
        }
        return new Object();
    }

    private Class<?> getCandidateOperatorClazz(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        UserActionLogger userActionLogger = method.getAnnotation(UserActionLogger.class);
        return userActionLogger.userSessionFetchAdapter();
    }

    private void publishEvent(ServerHttpRequest request) {
        // 具体业务逻辑
        System.out.println("213");
    }
}
