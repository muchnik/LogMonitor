package com.muchnik.logmonitor.persistence.datasource;

import org.apache.commons.dbcp.BasicDataSource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static com.muchnik.logmonitor.util.ConfigLoader.*;

public class DataSource {
    private static volatile DataSource datasource;
    private BasicDataSource ds;

    private DataSource() throws IOException, SQLException, PropertyVetoException {
        ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUsername(getProperty(DB_USER_NAME_KEY));
        ds.setPassword(getProperty(DB_PASSWORD_KEY));
        ds.setUrl(getProperty(DB_URL_KEY));
    }

    public static DataSource getInstance() throws IOException, SQLException, PropertyVetoException {
        DataSource localInstance = datasource;
        if (localInstance == null) {
            synchronized (DataSource.class) {
                localInstance = datasource;
                if (localInstance == null) {
                    datasource = localInstance = new DataSource();
                }
            }
        }
        return localInstance;
    }

    public Connection getConnection() throws SQLException {
        return this.ds.getConnection();
    }

    public void close(){
        try {
            ds.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}