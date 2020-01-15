//package zsh.springboot.aspect.aop;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.google.common.collect.Maps;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.swagger.annotations.ApiOperation;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.*;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.DefaultParameterNameDiscoverer;
//import org.springframework.core.ParameterNameDiscoverer;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.security.web.server.context.SecurityContextServerWebExchange;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
//
//@Slf4j
//@Data
//@Aspect
//@Component
//public class UserActionLoggerAspect {
//
//    private final static String LINE_SEPARATOR = System.lineSeparator();
//    private final String POINT_CUT = "@annotation(zsh.springboot.aspect.aop.UserActionLoggerAspect)";
//
//    private ThreadLocal<RosLogsRecordModel> threadPerlog = new ThreadLocal();
//
//    @Around(value=POINT_CUT)
//    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
//        RosLogsRecordModel logEntity = new RosLogsRecordModel();
//        //获取到请求参数
//        Map<String, Object> fieldsName = getFieldsName(proceedingJoinPoint);
//        Object param = fieldsName.get(getAspectLogParam(proceedingJoinPoint));
//        if (param!=null){
//            logEntity.setRequestParam(JSON.toJSONString(param));
//            Map map = JSONObject.parseObject(JSON.toJSONString(param), Map.class);
//            Integer type = (Integer)map.get(getParamType(proceedingJoinPoint));
//            logEntity.setLogType(type);
//
//        }
//        //设置type类型
//        String aspectLogType = getAspectLogType(proceedingJoinPoint);
//        if (StringUtils.isNotBlank(aspectLogType)){
//            logEntity.setLogType(Integer.valueOf(aspectLogType));
//        }
//        threadPerlog.set(logEntity);
//        Object result = proceedingJoinPoint.proceed();
//        //设置response param
//        logEntity.setResponseParam(JSON.toJSONString(result));
//        return result;
//    }
//
//    @Before(value=POINT_CUT)
//    public void doBefore(JoinPoint joinPoint) throws Throwable{
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        String authorization = null;
//        if (Objects.isNull(attributes)){
//            // WebFlux方式拦截
//            SecurityContextServerWebExchange securityContextServerWebExchange = null;
//            for (Object arg : joinPoint.getArgs()) {
//                if (arg instanceof SecurityContextServerWebExchange) {
//                    securityContextServerWebExchange = (SecurityContextServerWebExchange) arg;
//                }
//            }
//
//            ServerHttpRequest request = securityContextServerWebExchange.getRequest();
//            HttpHeaders headers = request.getHeaders();
//            authorization = headers.get("Authorization").get(0);
//            RosLogsRecordModel recordModel=threadPerlog.get();
//            if (authorization !=null){
//                Claims claims = Jwts.parser().setSigningKey("unv9145n_2.d8@ax").parseClaimsJws(authorization.substring(7)).getBody();
//                String account = claims.get("userAccount", String.class);
//                recordModel.setAccount(account);
//            }
//            recordModel.setUrl(request.getURI()==null?null:request.getURI().toString());
//            recordModel.setIp(request.getRemoteAddress().getHostString());
//            recordModel.setClassUrl(joinPoint.getSignature().getDeclaringTypeName());
//            recordModel.setMethodType(request.getMethodValue());
//            recordModel.setModule(getAspectLogModule(joinPoint));
//            recordModel.setMethod(joinPoint.getSignature().getName());
//            Map<String, String> map = getAspectLogDescription(joinPoint);
//            recordModel.setRecord(map.get("record"));
//            recordModel.setEnRecord(map.get("enRecord"));
//        }else {
//            // Servlet方式拦截
//            HttpServletRequest request = attributes.getRequest();
//            RosLogsRecordModel recordModel=threadPerlog.get();
//            if (authorization !=null){
//                Claims claims = Jwts.parser().setSigningKey("unv9145n_2.d8@ax").parseClaimsJws(authorization.substring(7)).getBody();
//                String account = claims.get("userAccount", String.class);
//                recordModel.setAccount(account);
//            }
//            recordModel.setUrl(request.getRequestURL()==null?null:request.getRequestURL().toString());
//            recordModel.setIp(request.getRemoteAddr());
//            recordModel.setClassUrl(joinPoint.getSignature().getDeclaringTypeName());
//            recordModel.setMethodType(request.getMethod());
//            recordModel.setModule(getAspectLogModule(joinPoint));
//            recordModel.setMethod(joinPoint.getSignature().getName());
//            Map<String, String> map = getAspectLogDescription(joinPoint);
//            recordModel.setRecord(map.get("record"));
//            recordModel.setEnRecord(map.get("enRecord"));
//
//        }
//    }
//
//    @After(value=POINT_CUT)
//    public void doAfter() throws Throwable{
//        RosLogsRecordModel recordModel=threadPerlog.get();
//        sender.sendMessage("ros-logs-topic",JSON.toJSONString(recordModel));
//        log.info("**************************** End ****************************{}" , LINE_SEPARATOR);
//    }
//
//    @AfterThrowing(value=POINT_CUT, throwing = "e")
//    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
//        handleLog(joinPoint, e, null);
//    }
//
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
//
//    public Map<String,String> getAspectLogDescription(JoinPoint joinPoint){
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        ApiOperation webLog = method.getAnnotation(ApiOperation.class);
//        HashMap<String, String> hashMap = Maps.newHashMap();
//        hashMap.put("record",webLog.notes());
//        hashMap.put("enRecord",webLog.value());
//        return hashMap;
//    }
//
//
//    public String getAspectLogModule(JoinPoint joinPoint){
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        UserActionLogger webLog = method.getAnnotation(UserActionLogger.class);
//        return webLog.module();
//    }
//
//    public String getAspectLogParam(JoinPoint joinPoint){
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        UserActionLogger webLog = method.getAnnotation(UserActionLogger.class);
//        return webLog.param();
//    }
//
//    public String getAspectLogType(JoinPoint joinPoint){
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        UserActionLogger webLog = method.getAnnotation(UserActionLogger.class);
//        return webLog.type();
//    }
//
//    public String getParamType(JoinPoint joinPoint){
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        UserActionLogger webLog = method.getAnnotation(UserActionLogger.class);
//        return webLog.paramType();
//    }
//
//    private static Map<String, Object> getFieldsName(ProceedingJoinPoint joinPoint) {
//        // 参数值
//        Object[] args = joinPoint.getArgs();
//        ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        String[] parameterNames = pnd.getParameterNames(method);
//        Map<String, Object> paramMap = new HashMap<>(32);
//        for (int i = 0; i < parameterNames.length; i++) {
//            paramMap.put(parameterNames[i], args[i]);
//        }
//        return paramMap;
//    }
//
//    /**
//     * 是否存在注解，如果存在就获取
//     */
//    private UserActionLogger getAnnotationLog(JoinPoint joinPoint) throws Exception {
//        Signature signature = joinPoint.getSignature();
//        MethodSignature methodSignature = (MethodSignature) signature;
//        Method method = methodSignature.getMethod();
//
//        if (method != null)
//        {
//            return method.getAnnotation(UserActionLogger.class);
//        }
//        return null;
//    }
//
//}
