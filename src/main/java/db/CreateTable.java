package db;

/**
 * Created by Liam on 14/02/2019.
 */

public class CreateTable extends DatabaseInteraction{

    private static final String tableName = "events_table";

    private static final String sqlStr = "CREATE TABLE " + tableName +" (\n" +
            "   id VARCHAR(50) NOT NULL,\n" +
            "   duration INT NOT NULL,\n" +
            "   type VARCHAR(50),\n" +
            "   host VARCHAR(50),\n" +
            "   alert BOOLEAN NOT NULL\n" +
            ");";

    public void create(){
        exec(sqlStr);
    }

    public static String getTableName(){
        return tableName;
    }
}