package com.example.back.config;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.Connector;
import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.Session;

public class EclipselinkCustomizer implements SessionCustomizer {

    private static DataSource dbcpds;

    public static void setDataSource(DataSource dataSource) {
        dbcpds = dataSource;
    }

    @Override
    public void customize(Session session) throws Exception {
        if (dbcpds != null) {
            DatabaseLogin login = (DatabaseLogin) session.getLogin();
            login.setConnector(new Connector() {

                @Override
                public Object clone() {
                    return this;
                }

                @Override
                public Connection connect(Properties properties, Session session) {
                    try {
                        return dbcpds.getConnection();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void toString(PrintWriter writer) {
                    writer.println("Apache DBCP DataSource Connector");
                }

                @Override
                public String getConnectionDetails() {
                    return "Apache DBCP DataSource Connector";
                }
            });
        }
    }

}
