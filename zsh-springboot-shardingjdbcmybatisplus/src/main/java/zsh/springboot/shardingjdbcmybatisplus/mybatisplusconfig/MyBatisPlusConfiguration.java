package zsh.springboot.shardingjdbcmybatisplus.mybatisplusconfig;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisPlusConfiguration {

    /**
     * mybatis-plus SQL执行效率插件【生产环境可以关闭】 打印sql语句
     * 3.2以上的版本移除了该插件
     */
//    @Bean
//    public PerformanceInterceptor performanceInterceptor() {
//        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
//        //格式化sql语句
//        Properties properties = new Properties();
//        properties.setProperty("format", "false");
//        performanceInterceptor.setProperties(properties);
//        return new PerformanceInterceptor();
//    }


    /**
     * 分页插件，自动识别数据库类型 多租户，请参考官网【插件扩展】
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
