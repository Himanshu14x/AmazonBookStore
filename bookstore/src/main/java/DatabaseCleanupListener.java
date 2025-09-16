import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import com.DB.DynamoDBClientProvider;

/**
 * ServletContextListener that performs cleanup on web application shutdown.
 * For this project we close the DynamoDbClient created by DynamoDBClientProvider.
 
 */
@WebListener
public class DatabaseCleanupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // nothing needed here for now
        sce.getServletContext().log("DatabaseCleanupListener initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().log("DatabaseCleanupListener contextDestroyed() called - performing cleanup.");

        // Close the DynamoDB client if it was created
        try {
            DynamoDBClientProvider.closeClient();
            sce.getServletContext().log("DynamoDbClient closed successfully.");
        } catch (Throwable t) {
            sce.getServletContext().log("Error while closing DynamoDbClient.", t);
        }

        sce.getServletContext().log("Cleanup finished.");
    }
}
