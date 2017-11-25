package com.muchnik.logmonitor;

import com.muchnik.logmonitor.persistence.datasource.DataSource;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@WebListener
public class ServerListener implements ServletContextListener {
    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.execute(new LogMonitorApplication());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LogMonitorApplication.getInitializer().interrupt();
        scheduler.shutdownNow();
        try {
            DataSource.getInstance().close();
        } catch (IOException | SQLException | PropertyVetoException e) {
            e.printStackTrace();
        }
        deregisterDrivers(getDrivers()); //deregistering JDBC driver
    }

    private Enumeration<Driver> getDrivers() {
        return DriverManager.getDrivers();
    }

    private void deregisterDrivers(Enumeration<Driver> drivers) {
        while (drivers.hasMoreElements()) {
            deregisterDriver(drivers.nextElement());
        }
    }

    private void deregisterDriver(Driver driver) {
        try {
            DriverManager.deregisterDriver(driver);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
