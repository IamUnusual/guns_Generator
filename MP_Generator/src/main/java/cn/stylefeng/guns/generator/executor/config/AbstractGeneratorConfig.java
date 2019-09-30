package cn.stylefeng.guns.generator.executor.config;

import cn.hutool.core.io.FileUtil;
import cn.stylefeng.guns.generator.engine.base.GunsTemplateEngine;
import cn.stylefeng.guns.generator.engine.config.SqlConfig;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import cn.stylefeng.guns.generator.engine.SimpleTemplateEngine;
import cn.stylefeng.guns.generator.engine.config.ContextConfig;

import java.io.File;
import java.util.List;

/**
 * 代码生成的抽象配置
 *
 * @author fengshuonan
 * @date 2017-10-28-下午8:22
 */
public abstract class AbstractGeneratorConfig {

    /**
     * mybatis-plus代码生成器配置
     */
    GlobalConfig globalConfig = new GlobalConfig();

    DataSourceConfig dataSourceConfig = new DataSourceConfig();

    StrategyConfig strategyConfig = new StrategyConfig();

    PackageConfig packageConfig = new PackageConfig();

    TableInfo tableInfo = null;

    /**
     * Guns代码生成器配置
     */
    ContextConfig contextConfig = new ContextConfig();

    SqlConfig sqlConfig = new SqlConfig();

    protected abstract void config();

    public void init() {
        config();

        packageConfig.setService(contextConfig.getProPackage() + ".modular." + contextConfig.getModuleName() + ".service");
        packageConfig.setServiceImpl(contextConfig.getProPackage() + ".modular." + contextConfig.getModuleName() + ".service.impl");

        //controller没用掉,生成之后会自动删掉
        packageConfig.setController("MP_Generator_code");

        if (!contextConfig.getEntitySwitch()) {
            packageConfig.setEntity("MP_Generator_code");
        }

        if (!contextConfig.getDaoSwitch()) {
            packageConfig.setMapper("MP_Generator_code");
            packageConfig.setXml("MP_Generator_code");
        }

        if (!contextConfig.getServiceSwitch()) {
            packageConfig.setService("MP_Generator_code");
            packageConfig.setServiceImpl("MP_Generator_code");
        }

    }

    /**
     * 删除不必要的代码
     */
    public void destory() {
        String outputDir = globalConfig.getOutputDir() + "/MP_Generator_code";
        FileUtil.del(new File(outputDir));
    }

    public AbstractGeneratorConfig() {
    }

    public void doMpGeneration() {
        init();
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(globalConfig);
        autoGenerator.setDataSource(dataSourceConfig);
        autoGenerator.setStrategy(strategyConfig);
        autoGenerator.setPackageInfo(packageConfig);
        autoGenerator.execute();
        destory();

        //获取table信息,用于guns代码生成
        List<TableInfo> tableInfoList = autoGenerator.getConfig().getTableInfoList();
        if (tableInfoList != null && tableInfoList.size() > 0) {
            this.tableInfo = tableInfoList.get(0);
        }
    }

    public void doGunsGeneration() {
        GunsTemplateEngine GunsTemplateEngine = new SimpleTemplateEngine();
        GunsTemplateEngine.setContextConfig(contextConfig);
        sqlConfig.setConnection(dataSourceConfig.getConn());
        GunsTemplateEngine.setSqlConfig(sqlConfig);
        GunsTemplateEngine.setTableInfo(tableInfo);
        GunsTemplateEngine.start();
    }
}
