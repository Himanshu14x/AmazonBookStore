import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

@WebListener
public class DatabaseCleanupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // nothing needed here usually
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 1) Deregister JDBC drivers registered by this webapp's ClassLoader
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            // Only deregister drivers that were registered by this webapp's classloader
            if (driver.getClass().getClassLoader() == cl) {
                try {
                    DriverManager.deregisterDriver(driver);
                    sce.getServletContext().log("Deregistered JDBC driver: " + driver);
                } catch (SQLException ex) {
                    sce.getServletContext().log("Error deregistering driver " + driver, ex);
                }
            } else {
                sce.getServletContext().log("Not deregistering JDBC driver as it does not belong to this ClassLoader: " + driver);
            }
        }

        // 2) Ask MySQL driver to shut down its AbandonedConnectionCleanupThread (if present)
        try {
            // MySQL Connector/J 8.x provides:
            com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
            sce.getServletContext().log("Called AbandonedConnectionCleanupThread.checkedShutdown()");
        } catch (Throwable t) {
            // older driver package name fallback (very old drivers) or absent method
            try {
                // Older connector had class com.mysql.jdbc.AbandonedConnectionCleanupThread
                Class<?> cleanupClass = Class.forName("com.mysql.jdbc.AbandonedConnectionCleanupThread");
                java.lang.reflect.Method m = cleanupClass.getMethod("shutdown");
                m.invoke(null);
                sce.getServletContext().log("Invoked com.mysql.jdbc.AbandonedConnectionCleanupThread.shutdown()");
            } catch (Throwable t2) {
                // nothing more to do; log and continue
                sce.getServletContext().log("No MySQL AbandonedConnectionCleanupThread found or failed to shutdown it.", t2);
            }
        }
    }
}
