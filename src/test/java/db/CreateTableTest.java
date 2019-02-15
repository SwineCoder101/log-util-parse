package db;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Liam on 14/02/2019.
 */
public class CreateTableTest extends TestHelper{
    CreateTable createTable;

    @Before
    public void init(){
        //DatabaseApplicationServer dbs = new DatabaseApplicationServer();
        createTable = new CreateTable();
    }

    @Test
    public void shouldCreateTableWithoutException(){
        createTable.create();
        exec("select * from EVENTS_TABLE");
    }
}
