package com.foufou;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableTransactionManagement //开启注解方式的事务管理
//@MapperScan("com.foufou.mapper")
@Slf4j
public class QMApplication {

    public static void main(String[] args) {
        SpringApplication.run(QMApplication.class, args);
        log.info("server start");
    }

}
