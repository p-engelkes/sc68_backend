package com.pengelkes.jooq;

import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created by pengelkes on 22.11.2016.
 */
@Configuration
@ComponentScan({"com.pengelkes.backend.jooq.tables"})
@EnableTransactionManagement
public class PersistenceContext
{
    @Bean
    @Primary
    public DataSource dataSource()
    {
        Jdbc3PoolingDataSource dataSource = new Jdbc3PoolingDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5433/sc68");
        dataSource.setUser("lagoon");
        dataSource.setPassword("lagoon");

        return dataSource;
    }

    @Bean
    public TransactionAwareDataSourceProxy transactionAwareDataSource()
    {
        return new TransactionAwareDataSourceProxy(dataSource());
    }

    @Bean
    public DataSourceTransactionManager transactionManager()
    {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public DataSourceConnectionProvider connectionProvider()
    {
        return new DataSourceConnectionProvider(transactionAwareDataSource());
    }

    @Bean
    public ExceptionTranslator exceptionTransformer()
    {
        return new ExceptionTranslator();
    }

    @Bean
    public DefaultDSLContext dsl()
    {
        return new DefaultDSLContext(configuration());
    }

    @Bean
    public DefaultConfiguration configuration()
    {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.set(connectionProvider());
        jooqConfiguration.set(new DefaultExecuteListenerProvider(exceptionTransformer()));

        SQLDialect dialect = SQLDialect.POSTGRES;
        jooqConfiguration.set(dialect);

        return jooqConfiguration;
    }
}
