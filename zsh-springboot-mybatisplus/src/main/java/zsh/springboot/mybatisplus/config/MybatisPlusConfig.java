package zsh.springboot.mybatisplus.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Configuration
public class MybatisPlusConfig {

    private static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    @Autowired
    private Environment environment;

    @Bean("ds0DataSource")
    @ConfigurationProperties("spring.datasource.ds-0")
    public DataSource ds0DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "ds0DataSourceTxManager")
    public DataSourceTransactionManager ds0DataSourceTxManager(@Qualifier("ds0DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("ds0SqlSessionFactory")
    @ConditionalOnBean(name="ds0DataSource")
    public SqlSessionFactory ds0SqlSessionFactory(@Autowired @Qualifier("ds0DataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(getResources(environment.getProperty("mybatis-plus.ds-0.mapper-locations")));
//        GlobalConfig globalConfig = new GlobalConfig();
//        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig().setIdType(IdType.ID_WORKER);
//        globalConfig.setDbConfig(dbConfig);
//        sqlSessionFactoryBean.setGlobalConfig(globalConfig);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean("ds1DataSource")
    @ConfigurationProperties("spring.datasource.ds-1")
    public DataSource ds1DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "ds1DataSourceTxManager")
    public DataSourceTransactionManager ds1DataSourceTxManager(@Qualifier("ds1DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("ds1SqlSessionFactory")
    @ConditionalOnBean(name="ds1DataSource")
    public SqlSessionFactory ds1SqlSessionFactory(@Autowired @Qualifier("ds1DataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(getResources(environment.getProperty("mybatis-plus.ds-1.mapper-locations")));
//        GlobalConfig globalConfig = new GlobalConfig();
//        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig().setIdType(IdType.ID_WORKER);
//        globalConfig.setDbConfig(dbConfig);
//        sqlSessionFactoryBean.setGlobalConfig(globalConfig);
        return sqlSessionFactoryBean.getObject();
    }

    public Resource[] resolveMapperLocations(String[] mapperLocations) {
        return Stream.of(Optional.ofNullable(mapperLocations).orElse(new String[0]))
            .flatMap(location -> Stream.of(getResources(location)))
            .toArray(Resource[]::new);
    }

    private Resource[] getResources(String location) {
        try {
            return resourcePatternResolver.getResources(location);
        } catch (IOException e) {
            return new Resource[0];
        }
    }

    /**
     * mybatis-plus分页插件<br>
     * 文档：http://mp.baomidou.com<br>
     */
//    @Bean
//    public PaginationInterceptor paginationInterceptor() {
//        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//        /*
//         * 【测试多租户】 SQL 解析处理拦截器<br>
//         * 这里固定写成住户 1 实际情况你可以从cookie读取，因此数据看不到 【 麻花藤 】 这条记录（ 注意观察 SQL ）<br>
//         */
//        List<ISqlParser> sqlParserList = new ArrayList<>();
//        TenantSqlParser tenantSqlParser = new TenantSqlParser();
//        tenantSqlParser.setTenantHandler(new TenantHandler() {
//
//            @Override
//            public Expression getTenantId(boolean where) {
//                return new LongValue(1L);
//            }
//
//            @Override
//            public String getTenantIdColumn() {
//                return "tenant_id";
//            }
//
//            @Override
//            public boolean doTableFilter(String tableName) {
//                // 这里可以判断是否过滤表
//                /*if ("user".equals(tableName)) {
//                    return true;
//                }*/
//                return false;
//            }
//        });
//
//        sqlParserList.add(tenantSqlParser);
//        paginationInterceptor.setSqlParserList(sqlParserList);
////        paginationInterceptor.setSqlParserFilter(new ISqlParserFilter() {
////            @Override
////            public boolean doFilter(MetaObject metaObject) {
////                MappedStatement ms = PluginUtils.getMappedStatement(metaObject);
////                // 过滤自定义查询此时无租户信息约束【 麻花藤 】出现
////                if ("com.baomidou.springboot.mapper.UserMapper.selectListBySQL".equals(ms.getId())) {
////                    return true;
////                }
////                return false;
////            }
////        });
//        return paginationInterceptor;
//    }

    /**
     * 相当于顶部的：
     * {@code @MapperScan("com.baomidou.springboot.mapper*")}
     * 这里可以扩展，比如使用配置文件来配置扫描Mapper的路径
     */
//    @Bean
//    public MapperScannerConfigurer mapperScannerConfigurer() {
//        MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
//        scannerConfigurer.setBasePackage("com.baomidou.springboot.mapper*");
//        return scannerConfigurer;
//    }

//    @Bean
//    public H2KeyGenerator getH2KeyGenerator() {
//        return new H2KeyGenerator();
//    }

    /**
     * 性能分析拦截器，不建议生产使用
     */
//    @Bean
//    public PerformanceInterceptor performanceInterceptor(){
//        return new PerformanceInterceptor();
//    }
}
