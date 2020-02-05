package zsh.springboot.shardingjdbcmybatisplus.mybatisplustool;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

public class MyBatisPlusAutoGenerator {

    public static void generateCode(String dbUrl,String username,String password,String packageName, String... tableNames) {
//        String packageName = "com.xxx.xxx.xxx.xxx";
        //generateByTables(packageName, "t_student", "t_city", "t_idcard");
        generateByTables(dbUrl,username,password,packageName, tableNames);
    }

    private static void generateByTables(String dbUrl,String username,String password,String packageName, String... tableNames) {
        // 自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<>();
        //如 每张表都有一个创建时间、修改时间
        //而且这基本上就是通用的了，新增时，创建时间和修改时间同时修改
        //修改时，修改时间会修改，
        //虽然像Mysql数据库有自动更新几只，但像ORACLE的数据库就没有了，
        //使用公共字段填充功能，就可以实现，自动按场景更新了。
        //如下是配置
//        TableFill createField = new TableFill("create_time", FieldFill.INSERT);
//        TableFill updateField = new TableFill("update_time", FieldFill.INSERT_UPDATE);
//        tableFillList.add(createField);
//        tableFillList.add(updateField);
        // 数据库信息
//        String dbUrl = "jdbc:mysql://localhost:3306/mybatis-plus?useSSL=true";
//        String dbUrl = "jdbc:mysql://localhost:3306/ros?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true";
        DataSourceConfig dataSourceConfig = new DataSourceConfig()
            .setDbType(DbType.MYSQL)
            .setUrl(dbUrl)
            .setUsername(username)
            .setPassword(password)
            .setDriverName("com.mysql.jdbc.Driver") // mysql 5
//            .setDriverName("com.mysql.cj.jdbc.Driver") // mysql 8
                /*.setTypeConvert(new MySqlTypeConvert() {
                    @Override
                    public PropertyInfo processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                        //.....
                        // 当发现生成的类型并不能满足你的要求时，可以去这里看，然后重写
                    }
                })*/;
        String projectPath = System.getProperty("user.dir");
        // 配置
        GlobalConfig config = new GlobalConfig()
                .setActiveRecord(false)
//                .setOutputDir("/Users/fengwenyi/Workspace/file/codeGen")
//                .setOutputDir("d:/codeGen")
//                .setOutputDir(projectPath + "/src/main/java")
                .setOutputDir(projectPath + "/zsh-springboot-shardingjdbcmybatisplus/src/main/java")
                .setFileOverride(true)
                .setActiveRecord(true)// 不需要ActiveRecord特性的请改为false
                .setEnableCache(false)// XML 二级缓存
                .setBaseResultMap(true)// XML ResultMap
                .setBaseColumnList(false)// XML columList
                .setKotlin(false) //是否生成 kotlin 代码
                // 自定义文件命名，注意 %s 会自动填充表实体属性！
                .setMapperName("%sDao")
                .setXmlName("%sMapper")
                .setServiceName("%sService")
                .setServiceImplName("%sServiceImpl")
                .setControllerName("%sController")
                .setDateType(DateType.ONLY_DATE) //只使用 java.util.date 代替
                .setIdType(IdType.ASSIGN_ID)
//                .setSwagger2(true) // model swagger2
                .setOpen(false) // 是否打开输出目录
                ;
//                if (!serviceNameStartWithI)
//                    config.setServiceName("%sService");


        StrategyConfig strategyConfig = new StrategyConfig()
                .setCapitalMode(true) // 全局大写命名 ORACLE 注意
                //.setDbColumnUnderline(true)
//                .setTablePrefix("t_")// 此处可以修改为您的表前缀(数组)
                .setNaming(NamingStrategy.underline_to_camel) // 表名生成策略
                .setInclude(tableNames)//修改替换成你需要的表名，多个表名传数组
                //.setExclude(new String[]{"test"}) // 排除生成的表
                .setEntityLombokModel(true) // lombok实体
                .setEntityBuilderModel(false) // 【实体】是否为构建者模型（默认 false）
                .setEntityColumnConstant(false) // 【实体】是否生成字段常量（默认 false）// 可通过常量名获取数据库字段名 // 3.x支持lambda表达式
                .setRestControllerStyle(true)
                //.setLogicDeleteFieldName("is_delete") // 逻辑删除属性名称
                //.setEntityTableFieldAnnotationEnable
                .entityTableFieldAnnotationEnable(true);

        // 包信息配置
        PackageConfig packageConfig = new PackageConfig()
                .setModuleName(packageName)
                .setParent("zsh.springboot.shardingjdbcmybatisplus")
//                .setParent(packageName)
                .setController("controller")
                .setEntity("model")
                .setMapper("dao")
                .setXml("mapper")

                /*
                // 共同构建成包名
                .setParent("com.fengwenyi")
                .setModuleName("model")
                */;

        // 执行器
        new AutoGenerator()
            .setGlobalConfig(config)
            .setDataSource(dataSourceConfig)
            .setStrategy(strategyConfig)
            .setPackageInfo(packageConfig)
            .execute();
    }

    public static void main(String[] args) {

        String[] ds0Tables = {"t_order", "t_user", "t_order_item", "t_address"};

        generateCode(
                "jdbc:mysql://106.54.211.146:33006/ds_0?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true",
                "root",
                "mima",
                "ds", ds0Tables);
    }
}
