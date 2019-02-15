package db;

import com.fasterxml.jackson.databind.JsonNode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Liam on 14/02/2019.
 */
public class TestHelper extends DatabaseInteraction {

    public String trim(JsonNode jsonNode){
        return jsonNode.toString().replace("\"","");
    }

    protected Statement stmt;
    protected ResultSet result;

    public void execSelect(String sqlStr){
        Connection con = null;
        stmt = null;
        result = null;

        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost:999/LogEventDb", "SA", "");
            stmt = con.createStatement();
            result = stmt.executeQuery(sqlStr);
            stmt.getGeneratedKeys();
        }  catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
