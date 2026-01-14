package com.example.back.datasource;

import java.time.Duration;

import org.apache.commons.dbcp2.BasicDataSource;

import com.example.back.config.EclipselinkCustomizer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DBCPDataSource {

    private BasicDataSource dataSource;

    @PostConstruct
    public void init() {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/workers");
        dataSource.setUsername("postgres");
        dataSource.setPassword("password");
        dataSource.setInitialSize(5);
        dataSource.setMaxTotal(20);
        dataSource.setMaxWait(Duration.ofMinutes(5));
        dataSource.setMinIdle(5);
        dataSource.setValidationQuery("select 1");

        EclipselinkCustomizer.setDataSource(dataSource);
    }

    @PreDestroy
    public void close() throws Exception {
        dataSource.close();
    }

    public BasicDataSource getDataSource() {
        return dataSource;
    }
}
