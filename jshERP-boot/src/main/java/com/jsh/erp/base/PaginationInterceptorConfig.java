package com.jsh.erp.base;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * description: PaginationInterceptor <br>
 * date: 2022/5/9 19:37 <br>
 * author: HKZX <br>
 * version: 1.0 <br>
 */
//@EnableTransactionManagement
@Configuration
@MapperScan("com.jsh.erp.datasource.mappers.*")
public class PaginationInterceptorConfig {
    /**
     * 分页插件 3.5.X
     *
     * @author zhengkai.blog.csdn.net
     */
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInterceptor.setMaxLimit(-1L);
        paginationInterceptor.setOverflow(true);
        paginationInterceptor.setDbType(DbType.MYSQL);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setOptimizeJoin(true);
        return paginationInterceptor;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.setInterceptors(Arrays.asList(paginationInnerInterceptor()));
        return mybatisPlusInterceptor;
    }
}