import db.CreateTable;
import db.TestHelper;
import domain.Event;
import middleware.EventConsumers;
import middleware.EventProducers;
import middleware.MiddlewareService;
import org.junit.Test;

import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by Liam on 14/02/2019.
 */
public class EventProducerConsumerTest extends TestHelper{


    @Test
    public void shouldSuccessfullyCreateProducerAndSendNormalEventsToBePersisted(){

        //Given a normal event with a middlewareservice
        Event event = new Event();
        MiddlewareService middlewareService = new MiddlewareService();
        middlewareService.start();


        //When spinning up 4 jms queues and sending 100 events down
        try {
            EventProducers eventProducers = new EventProducers(4,middlewareService);
            EventConsumers eventConsumers = new EventConsumers(eventProducers.getQueues(),middlewareService);
            eventConsumers.listen();

            for (int i =0; i<100; i++){
                event.setId("test"+i);
                eventProducers.sendEvent(event);
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        //Then make sure 100 normal events are in db
        execSelect("select * from " + CreateTable.getTableName() + " where id = '123Test2'");

        int count =0;

        try {
            while(result.next()){
                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //assertThat(count,equalTo(100));


    }
}
