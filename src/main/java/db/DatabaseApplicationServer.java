package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.hsqldb.Server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Liam on 14/02/2019.
 */


public class DatabaseApplicationServer{

    private String path = "resources";
    private String databaseName = "LogEventDb";
    private int port = 999;
    private String url;
    private static Logger logger = LoggerFactory.getLogger(DatabaseApplicationServer.class);
    private int WAIT_TIME = 100;

    @PostConstruct
    public void start() {
        try {
            String databasePath = path + "/" + databaseName;
            url = "jdbc:hsqldb:hsql://localhost:" + port + "/" + databaseName;

            Server server = new Server();
            server.setDatabaseName(0, databaseName);

            server.setDatabasePath(0, databasePath);
            server.setPort(port);
            server.setSilent(true);
            server.start();
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");

            Connection conn = null;
            Statement state = null;

            try {
                conn = DriverManager.getConnection(url);
                state = conn.createStatement();
                state.executeUpdate("SHUTDOWN;");
            } catch (SQLException ex1) {
                logger.error(ex1.getMessage(), ex1);
            } finally {
                if (state != null) {
                    try {
                        state.close();
                        state = null;
                    } catch (SQLException ex1) {
                        logger.error(ex1.getMessage(), ex1);
                    }
                }
                if (conn != null) {
                    try {
                        conn.close();
                        conn = null;
                    } catch (SQLException ex1) {
                        logger.error(ex1.getMessage(), ex1);
                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }


    public static void main(String [] args){
        DatabaseApplicationServer databaseApplicationServer = new DatabaseApplicationServer();
        databaseApplicationServer.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //databaseApplicationServer.destroy();

    }

}
