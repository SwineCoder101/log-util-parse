package middleware;

import db.PersisterTask;
import domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * Created by Liam on 14/02/2019.
 */
public class EventListener  implements MessageListener {

    private PersisterTask persisterTask;

    final static Logger logger = LoggerFactory.getLogger(EventListener.class);

    public EventListener(){
        persisterTask = new PersisterTask();
    }

    //TODO handle applicaton log events
    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;

        try {
            Event event = (Event) objectMessage.getObject();
            System.out.println(" received " + event.getId());
            persisterTask.build(event);

            logger.debug("Submitting persistence task on " + event.getId() );

            persisterTask.run();
            message.acknowledge();

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
