package zsh.springboot.logs.aspect;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import zsh.springboot.logs.autoconfigure.ZshLoggerProperties;
import zsh.springboot.logs.model.ZshSystemLoggerModel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Aspect
@Component
public class ZshSystemLoggerAspect implements InitializingBean {

    @Autowired
    private ZshLoggerProperties zshLoggerProperties;
    private Set<Class<?>> excludePrintClass;

    //    @Pointcut("execution(* com.zsh.ros.*.*.*.*Controller.*(..))")
//    @Pointcut("execution(* com.zsh.ros.*.*.*.*Controller.*(..)) || execution(* com.zsh.ros.*.*.*.*Service.*(..))")
//    @Pointcut("@args(zsh.springboot.reactiveweb.aspect.ZshLoggerParam)")
//    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)"||"@annotation(org.springframework.web.bind.annotation.GetMapping)")
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void pointcut() {}

    private String getMappingEntryPoint(ProceedingJoinPoint pjp) {

        Signature signature = pjp.getSignature();

        String httpEntryPoint = "";
        String httpMethod = "";

        Annotation clazzAnnotation = AnnotationUtils.getAnnotation(signature.getDeclaringType(), RequestMapping.class);
        if (clazzAnnotation == null) {
            return httpEntryPoint;
        }
        AnnotationAttributes clazzAttributes = AnnotationUtils.getAnnotationAttributes(signature.getDeclaringType(), clazzAnnotation);
        String[] clazzAttributesEntryPoints = (String[]) clazzAttributes.get("value");
        if (clazzAttributesEntryPoints.length > 0 ) {
            httpEntryPoint = httpEntryPoint.concat(clazzAttributesEntryPoints[0].startsWith("/") ?
                    clazzAttributesEntryPoints[0] : "/" + clazzAttributesEntryPoints[0]);
        }
        Method method = ((MethodSignature)signature).getMethod();

        Annotation mappingAnnotation;
        if (AnnotationUtils.getAnnotation(method, GetMapping.class) != null) {
            mappingAnnotation = AnnotationUtils.getAnnotation(method, GetMapping.class);
            httpMethod = "GET";
        } else if (AnnotationUtils.getAnnotation(method, PostMapping.class) != null) {
            mappingAnnotation = AnnotationUtils.getAnnotation(method, PostMapping.class);
            httpMethod = "POST";
        } else if (AnnotationUtils.getAnnotation(method, PutMapping.class) != null) {
            mappingAnnotation = AnnotationUtils.getAnnotation(method, PutMapping.class);
            httpMethod = "PUT";
        } else if (AnnotationUtils.getAnnotation(method, DeleteMapping.class) != null) {
            mappingAnnotation = AnnotationUtils.getAnnotation(method, DeleteMapping.class);
            httpMethod = "DELETE";
        } else {
            mappingAnnotation = AnnotationUtils.getAnnotation(method, RequestMapping.class);
            RequestMapping requestMapping = (RequestMapping) mappingAnnotation;
            if (requestMapping.method().length > 0) {
                httpMethod = requestMapping.method()[0].name();
            }
        }
        if (mappingAnnotation == null) {
            return httpEntryPoint;
        }
        AnnotationAttributes methodAttributes = AnnotationUtils.getAnnotationAttributes(method, mappingAnnotation);
        String[] methodAttributesEntryPoints = (String[]) methodAttributes.get("value");
        if (methodAttributesEntryPoints.length > 0) {
            httpEntryPoint = httpEntryPoint.concat(methodAttributesEntryPoints[0].startsWith("/") ?
                    methodAttributesEntryPoints[0] : "/"+methodAttributesEntryPoints[0]);
        }
        return httpMethod+" "+httpEntryPoint;
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        // 定义返回值
        Object pjpResult = null, aspectResult = null;
        Signature signature = pjp.getSignature();
        String targetClass = signature.getDeclaringTypeName();
        String targetMethod = signature.getName();

        Method method = ((MethodSignature)signature).getMethod();
        RequestMapping annoOnMethod = AnnotationUtils.getAnnotation(method, RequestMapping.class);

        if (annoOnMethod == null) {
            return pjp.proceed();
        }

        //执行开始时间
        StopWatch watch = new StopWatch();
        watch.start();

        try {
            pjpResult = pjp.proceed();
        }
        finally {
            //打印日志
            if (Objects.nonNull(pjpResult) && Mono.class.isAssignableFrom(pjpResult.getClass())) {
                Mono<Object> mono = (Mono<Object>) pjpResult;
                aspectResult = mono
                        .map(r -> {
                            watch.stop();
                            print(pjp, r, watch.getTotalTimeMillis());
                            return r;
                        })
                        .doOnError(ex -> {
                            watch.stop();
                            log.error("Class:[{}] method:[{}] fails [{}].", targetClass, targetMethod, ex);
                            print(pjp, null, watch.getTotalTimeMillis());
                        });
            } else if (Objects.nonNull(pjpResult) && Flux.class.isAssignableFrom(pjpResult.getClass())) {
                Flux<Object> flux = (Flux<Object>)pjpResult;
                aspectResult = flux
                        .doOnError(ex->{
                            watch.stop();
                            log.error("Class:[{}] method:[{}] fails [{}].", targetClass, targetMethod, ex);
                            print(pjp, null, watch.getTotalTimeMillis());
                        })
                        .doOnComplete(()->{
                            watch.stop();
                            print(pjp, null, watch.getTotalTimeMillis());
                        });
            } else {
                watch.stop();
                print(pjp, pjpResult, watch.getTotalTimeMillis());
                aspectResult = pjpResult;
            }
        }
        return aspectResult;
    }

    /**
     * 打印日志
     * @param pjp   连接点
     * @param result    方法调用返回结果
     * @param elapsedTime   方法调用花费时间
     */
    private void print(ProceedingJoinPoint pjp, Object result, long elapsedTime) {
        ZshSystemLoggerModel model = new ZshSystemLoggerModel();
        hintSwaggerAnnotation(pjp, model);

        Signature signature = pjp.getSignature();
        String targetClass  = signature.getDeclaringTypeName();
        String targetMethod = signature.getName();
        model.setClassName(targetClass);
        model.setMethodName(targetMethod);
        model.setElapsedTime(elapsedTime);
        model.setEntryPoint(getMappingEntryPoint(pjp));

        Method method = ((MethodSignature)signature).getMethod();
        Parameter[] ps = method.getParameters();
        Object[] args = pjp.getArgs();

        List<String> argumentAsStringList = Arrays.stream(args)
                .filter(arg -> {
                    Set<Class<?>> filter = excludePrintClass.stream()
                            .filter(clazz -> {
                                return clazz.isAssignableFrom(arg.getClass());
                            })
                            .collect(Collectors.toSet());
                    return filter.isEmpty();
                })
                .map(JSON::toJSONString)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());
        if (args.length != 0) {
            model.setArguments(argumentAsStringList.toString());
        }
        if (Objects.nonNull(result)) {
            model.setResult(JSON.toJSONString(result));
        }
        if (model.isAsync()) {
            new Thread(()->log.info(model.format(), model.args())).start();
        } else {
            log.info(model.format(), model.args());
        }
    }

    /**
     * 如果有Swagger注解，则获取
     */
    private void hintSwaggerAnnotation(ProceedingJoinPoint pjp, ZshSystemLoggerModel model) {

        // 检查系统是否引入了Swagger
        try {
            Class.forName("io.swagger.annotations.ApiOperation");
        } catch (ClassNotFoundException e) {
            return;
        }

        Signature signature = pjp.getSignature();
        Method method = ((MethodSignature)signature).getMethod();

        Annotation anno = AnnotationUtils.getAnnotation(method, ApiOperation.class);
        if (anno != null) {
            ApiOperation apiOperation = (ApiOperation) anno;
            model.setLocation(apiOperation.value());
            model.setDescription(apiOperation.notes());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        excludePrintClass = zshLoggerProperties.getExcludePrintClass().stream()
                .map(str -> {
                    try {
                        return Class.forName(str);
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
