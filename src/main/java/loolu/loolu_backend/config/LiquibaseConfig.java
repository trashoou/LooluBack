package loolu.loolu_backend.config;

/*
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import liquibase.integration.spring.SpringLiquibase;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:config/liquibase/master.xml");
        liquibase.setDataSource(dataSource());
        return liquibase;
    }
}

 */



import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import liquibase.integration.spring.SpringLiquibase;

@Configuration
public class LiquibaseConfig {

    @Bean
    public SpringLiquibase liquibase(@Qualifier("taskExecutor") TaskExecutor taskExecutor,
                                     DataSource dataSource, Environment env) {

        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:db/changelog/master.xml");
        liquibase.setContexts(env.getProperty("liquibase.contexts"));
        liquibase.setDefaultSchema(env.getProperty("liquibase.default-schema"));
        liquibase.setDropFirst(Boolean.parseBoolean(env.getProperty("liquibase.drop-first")));
        liquibase.setShouldRun(Boolean.parseBoolean(env.getProperty("liquibase.enabled", "true")));
        return liquibase;
    }
}


