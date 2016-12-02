package com.pengelkes.database.jooq.hstore;

import org.jooq.*;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;
import java.util.HashMap;

public class PostgresHstoreBinding implements Binding<Object, HashMap<String, String>>
{
    // The converter does all the work
    @Override
    public Converter<Object, HashMap<String, String>> converter()
    {
        return new Converter<Object, HashMap<String, String>>()
        {
            @Override
            public HashMap<String, String> from(Object t)
            {
                if (t instanceof HashMap)
                    return (HashMap<String, String>) t;
                else
                    return null;
            }

            @Override
            public Object to(HashMap<String, String> u)
            {
                return u;
            }

            @Override
            public Class<Object> fromType()
            {
                return Object.class;
            }

            @Override
            public Class<HashMap<String, String>> toType()
            {
                HashMap<String, String> instance = new HashMap<>();


//                return (Class<HashMap<String, String>>) instance.getClass().getDeclaringClass();
                return (Class<HashMap<String, String>>) instance.getClass();
            }
        };
    }

    // Rending a bind variable for the binding context's value and casting it to the json type
    @Override
    public void sql(BindingSQLContext<HashMap<String, String>> ctx) throws SQLException
    {
        //This is the important line for storing, since the object value is transferred without
        //any jooq interaction directly to the database driver.
        ctx.render().sql(ctx.variable());
    }

    // Registering VARCHAR types for JDBC CallableStatement OUT parameters
    @Override
    public void register(BindingRegisterContext<HashMap<String, String>> ctx) throws SQLException
    {
        ctx.statement().registerOutParameter(ctx.index(), Types.OTHER);
    }

    // Converting the JsonElement to a String value and setting that on a JDBC PreparedStatement
    @Override
    public void set(BindingSetStatementContext<HashMap<String, String>> ctx) throws SQLException
    {
        ctx.statement().setObject(ctx.index(), ctx.value());
    }

    // Getting a String value from a JDBC ResultSet and converting that to a JsonElement
    @Override
    public void get(BindingGetResultSetContext<HashMap<String, String>> ctx) throws SQLException
    {
        ctx.convert(converter()).value(ctx.resultSet().getObject(ctx.index()));
    }

    // Getting a String value from a JDBC CallableStatement and converting that to a JsonElement
    @Override
    public void get(BindingGetStatementContext<HashMap<String, String>> ctx) throws SQLException
    {
        ctx.convert(converter()).value(ctx.statement().getObject(ctx.index()));
    }

    // Setting a value on a JDBC SQLOutput (useful for Oracle OBJECT types)
    @Override
    public void set(BindingSetSQLOutputContext<HashMap<String, String>> ctx) throws SQLException
    {
        throw new SQLFeatureNotSupportedException();
    }

    // Getting a value from a JDBC SQLInput (useful for Oracle OBJECT types)
    @Override
    public void get(BindingGetSQLInputContext<HashMap<String, String>> ctx) throws SQLException
    {
        throw new SQLFeatureNotSupportedException();
    }
}