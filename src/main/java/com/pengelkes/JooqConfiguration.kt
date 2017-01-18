package com.pengelkes

import com.pengelkes.properties.DatabaseProperties
import org.jooq.*
import org.jooq.impl.*
import org.postgresql.jdbc3.Jdbc3PoolingDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.sql.SQLException
import java.sql.SQLFeatureNotSupportedException
import java.sql.Types
import java.util.*
import javax.sql.DataSource

/**
 * Created by pengelkes on 23.12.2016.
 */
@Configuration
@ComponentScan("com.pengelkes.backend.jooq.tables")
@EnableTransactionManagement
open class PersistenceContext @Autowired constructor(private val databaseProperties: DatabaseProperties) {

    @Bean @Primary
    open fun dataSource(): DataSource {
        val dataSource = Jdbc3PoolingDataSource()
        dataSource.url = databaseProperties.getUrl()
        dataSource.user = databaseProperties.getUser()
        dataSource.password = databaseProperties.getPassword()

        return dataSource
    }

    @Bean
    open fun transactionAwareDataSource(): TransactionAwareDataSourceProxy {
        return TransactionAwareDataSourceProxy(dataSource())
    }

    @Bean
    open fun transactionManager(): DataSourceTransactionManager {
        return DataSourceTransactionManager(dataSource())
    }

    @Bean
    open fun connectionProvider(): DataSourceConnectionProvider {
        return DataSourceConnectionProvider(transactionAwareDataSource())
    }

    @Bean
    open fun exceptionTransformer(): ExceptionTranslator {
        return ExceptionTranslator()
    }

    @Bean
    open fun dsl(): DefaultDSLContext {
        return DefaultDSLContext(configuration())
    }

    @Bean
    open fun configuration(): DefaultConfiguration {
        val jooqConfiguration = DefaultConfiguration()
        jooqConfiguration.set(connectionProvider())
        jooqConfiguration.set(DefaultExecuteListenerProvider(exceptionTransformer()))

        val dialect = SQLDialect.POSTGRES
        jooqConfiguration.set(dialect)

        return jooqConfiguration
    }
}

class ExceptionTranslator : DefaultExecuteListener() {

    override fun exception(context: ExecuteContext) {
        val dialect = context.configuration().dialect()
        val translator = SQLErrorCodeSQLExceptionTranslator(dialect.name)

        context.exception(translator.translate("Access database using jOOQ", context.sql(), context.sqlException()))
    }
}

class PostgresHstoreBinding : Binding<Any, HashMap<String, String>> {
    // The converter does all the work
    override fun converter(): Converter<Any, HashMap<String, String>> {
        return object : Converter<Any, HashMap<String, String>> {
            override fun from(t: Any): HashMap<String, String>? {
                if (t is HashMap<*, *>)
                    return t as HashMap<String, String>
                else
                    return null
            }

            override fun to(u: HashMap<String, String>): Any {
                return u
            }

            override fun fromType(): Class<Any> {
                return Any::class.java
            }

            override fun toType(): Class<HashMap<String, String>> {
                val instance = HashMap<String, String>()


                //                return (Class<HashMap<String, String>>) instance.getClass().getDeclaringClass();
                return instance.javaClass
            }
        }
    }

    // Rending a bind variable for the binding context's value and casting it to the json type
    @Throws(SQLException::class)
    override fun sql(ctx: BindingSQLContext<HashMap<String, String>>) {
        //This is the important line for storing, since the object value is transferred without
        //any jooq interaction directly to the database driver.
        ctx.render().sql(ctx.variable())
    }

    // Registering VARCHAR types for JDBC CallableStatement OUT parameters
    @Throws(SQLException::class)
    override fun register(ctx: BindingRegisterContext<HashMap<String, String>>) {
        ctx.statement().registerOutParameter(ctx.index(), Types.OTHER)
    }

    // Converting the JsonElement to a String value and setting that on a JDBC PreparedStatement
    @Throws(SQLException::class)
    override fun set(ctx: BindingSetStatementContext<HashMap<String, String>>) {
        ctx.statement().setObject(ctx.index(), ctx.value())
    }

    // Getting a String value from a JDBC ResultSet and converting that to a JsonElement
    @Throws(SQLException::class)
    override fun get(ctx: BindingGetResultSetContext<HashMap<String, String>>) {
        ctx.convert(converter()).value(ctx.resultSet().getObject(ctx.index()))
    }

    // Getting a String value from a JDBC CallableStatement and converting that to a JsonElement
    @Throws(SQLException::class)
    override fun get(ctx: BindingGetStatementContext<HashMap<String, String>>) {
        ctx.convert(converter()).value(ctx.statement().getObject(ctx.index()))
    }

    // Setting a value on a JDBC SQLOutput (useful for Oracle OBJECT types)
    @Throws(SQLException::class)
    override fun set(ctx: BindingSetSQLOutputContext<HashMap<String, String>>) {
        throw SQLFeatureNotSupportedException()
    }

    // Getting a value from a JDBC SQLInput (useful for Oracle OBJECT types)
    @Throws(SQLException::class)
    override fun get(ctx: BindingGetSQLInputContext<HashMap<String, String>>) {
        throw SQLFeatureNotSupportedException()
    }
}