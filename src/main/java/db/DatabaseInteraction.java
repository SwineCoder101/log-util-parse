package db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by Liam on 14/02/2019.
 */
public class DatabaseInteraction {

    protected int result;
    protected Statement stmt;

    final static Logger logger = LoggerFactory.getLogger(DatabaseInteraction.class);

    public void exec(String sqlStr){
        Connection con = null;
        stmt = null;
        result = 0;

        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost:999/LogEventDb", "SA", "");
            stmt = con.createStatement();

            logger.debug("Running SQL statement: " + sqlStr);

            result = stmt.executeUpdate(sqlStr);
            stmt.getGeneratedKeys();
        }  catch (Exception e) {
            e.printStackTrace(System.out);
        }

    }
}
