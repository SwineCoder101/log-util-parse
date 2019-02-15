package db;

import domain.ApplicationServerLogEvent;
import domain.Event;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.sql.SQLException;

/**
 * Created by Liam on 14/02/2019.
 */
public class PersisterTaskTest extends TestHelper{

    @Test
    public void shouldInsertNormalEvent(){
        //Given nomal event
        Event event = new Event();
        event.setId("123Test");
        event.setDuration(4);
        event.setAlert(true);

        //When Executing Query
        PersisterTask persisterTask = new PersisterTask();
        persisterTask.build(event);
        persisterTask.run();

        //Can retrieve normal event row
        execSelect("select * from " + CreateTable.getTableName() + " where id = '123Test'");


        try {
            while(result.next()){
                assertThat(result.getString("id"),equalTo("123Test"));
                assertThat(result.getInt("duration"),equalTo(4));
                assertThat(result.getBoolean("alert"),equalTo(true));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void shoudInsertApplicationEvent(){
        //Given application event
        ApplicationServerLogEvent event = new ApplicationServerLogEvent("","");
        event.setId("123Test2");
        event.setType("appservertypetest");
        event.setHost("localhost");
        event.setDuration(4);
        event.setAlert(true);

        //When Executing Query
        PersisterTask persisterTask = new PersisterTask();
        persisterTask.build(event);
        persisterTask.run();

        //Can retrieve application event row
        execSelect("select * from " + CreateTable.getTableName() + " where id = '123Test2'");

        try {
            while(result.next()){
                assertThat(result.getString("id"),equalTo("123Test2"));
                assertThat(result.getString("host"),equalTo("localhost"));
                assertThat(result.getString("type"),equalTo("appservertypetest"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
