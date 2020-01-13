package zsh.springboot.importbean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

/**
 * ZshBean动态加载类
 * @author zhouchunchen
 */

@Slf4j
public class ZshBeanRegister implements ImportBeanDefinitionRegistrar
        , ResourceLoaderAware
        , BeanClassLoaderAware
        , EnvironmentAware
        , BeanFactoryAware
        , ApplicationContextAware {

    private ClassLoader classLoader;
    private BeanFactory beanFactory;
    private Environment environment;
    private ResourceLoader resourceLoader;
    private ApplicationContext applicationContext;

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        // Nothing
        log.info("HERE");
    }
     */

    /**
     * 注册Bean
     * @param importingClassMetadata
     * @param registry
     */
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        String basePackage = ((StandardAnnotationMetadata) importingClassMetadata).getIntrospectedClass().getPackage().getName();
        registerZshBeanDefinition(registry, basePackage);
    }


    private void registerZshBeanDefinition(BeanDefinitionRegistry beanDefinitionRegistry, String basePackage) {
        ClassPathScanningCandidateComponentProvider classScanner = getClassScanner();
        classScanner.setResourceLoader(this.resourceLoader);
        //指定只关注标注了@ZshBean注解的接口
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(ZshBean.class);
        classScanner.addIncludeFilter(annotationTypeFilter);
        Set<BeanDefinition> beanDefinitionSet = classScanner.findCandidateComponents(basePackage);
        for (BeanDefinition beanDefinition : beanDefinitionSet) {
            if (beanDefinition instanceof AnnotatedBeanDefinition) {
                registerZshBeans(((AnnotatedBeanDefinition) beanDefinition));
            }
        }
    }

    /**
    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        // Get the MyInterfaceScan annotation attributes
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(MyInterfaceScan.class.getCanonicalName());

        if (annotationAttributes != null) {
            String[] basePackages = (String[]) annotationAttributes.get("value");

            if (basePackages.length == 0){
                // If value attribute is not set, fallback to the package of the annotated class
                basePackages = new String[]{((StandardAnnotationMetadata) metadata).getIntrospectedClass().getPackage().getName()};
            }

            // using these packages, scan for interface annotated with MyCustomBean
            ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false, environment){
                // Override isCandidateComponent to only scan for interface
                @Override
                protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                    AnnotationMetadata metadata = beanDefinition.getMetadata();
                    return metadata.isIndependent() && metadata.isInterface();
                }
            };
            provider.addIncludeFilter(new AnnotationTypeFilter(MyCustomBean.class));

            // Scan all packages
            for (String basePackage : basePackages) {
                for (BeanDefinition beanDefinition : provider.findCandidateComponents(basePackage)) {
                    // Do the stuff about the bean definition
                    // For example, redefine it as a bean factory with custom atribute...
                    // then register it
                    registry.registerBeanDefinition(generateAName() , beanDefinition);
                    System.out.println(beanDefinition);
                }
            }
        }
    }
    */

    /**
     * 构造Class扫描器，设置了只扫描顶级接口，不扫描内部类
     * @return
     */
    private ClassPathScanningCandidateComponentProvider getClassScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            // 对于筛选出来的BeanDefinition，再做一层过滤。
            // 此处只过滤出接口类型，方便后续进行JDK代理
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                if (beanDefinition.getMetadata().isInterface()) {
                    try {
                        Class<?> target = ClassUtils.forName(beanDefinition.getMetadata().getClassName(), classLoader);
                        return !target.isAnnotation();  // 过滤注解类型
                    } catch (Exception ex) {
                        log.error("Scanner load class meet error: {}", ex);
                    }
                }
                return false;
            }
        };
    }

    /**
     * 创建动态代理，并动态注册到容器中
     * @param annotatedBeanDefinition
     */
    private void registerZshBeans(AnnotatedBeanDefinition annotatedBeanDefinition) {
        String className = annotatedBeanDefinition.getBeanClassName();
        ((DefaultListableBeanFactory) this.beanFactory).registerSingleton(className, createProxy(annotatedBeanDefinition));
    }

    /**
     * 创建动态代理
     * @param annotatedBeanDefinition
     * @return
     */
    private Object createProxy(AnnotatedBeanDefinition annotatedBeanDefinition) {
        try {
            AnnotationMetadata annotationMetadata = annotatedBeanDefinition.getMetadata();
            Class<?> target = Class.forName(annotationMetadata.getClassName());
            InvocationHandler ih = new InvocationHandler() {
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                    method.invoke(proxy, "123123");
                    log.info("{}", args);
                    return null;
                }
            };
            Object proxy = Proxy.newProxyInstance(ZshBeanParameter.class.getClassLoader(), new Class[] {target}, ih);
            return proxy;
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
