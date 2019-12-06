package zsh.springboot.mybatisplus.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(value = "zsh.springboot.mybatisplus.ds1.dao*", sqlSessionFactoryRef = "ds1SqlSessionFactory")
//这个注解，作用相当于下面的@Bean MapperScannerConfigurer，2者配置1份即可
public class Ds1DataSourceConfig {

}
