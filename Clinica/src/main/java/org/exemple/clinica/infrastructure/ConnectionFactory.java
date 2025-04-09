package org.exemple.clinica.infrastructure;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String RESOURCE_NAME = "PostgresDS";

    public Connection getConnection() throws NamingException, SQLException {
        return getDatasource().getConnection();
    }

    private DataSource getDatasource() throws NamingException {
        return  (DataSource) new InitialContext().lookup(RESOURCE_NAME);
    }

}
