package middleware;

import domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liam on 14/02/2019.
 */
public class EventProducers{

    private int numQs;
    private ObjectMessage eventMessage;
    private Session session;
    private Queue queue;
    private List<Queue> queues;
    private MessageProducer messageProducer;
    private List<MessageProducer> messageProducers;

    final static Logger logger = LoggerFactory.getLogger(EventProducers.class);

    private static int counter =0;

    public EventProducers (int numQs, MiddlewareService middlewareService){
        this.numQs=numQs;
        this.messageProducers = new ArrayList<>();
        queues = new ArrayList<>();

        logger.info("Creating " + numQs + " event producers");

        try {
            session = middlewareService.getSession();

            for (int i =0; i<numQs;i++){
                queue=session.createQueue("queue" + i);
                queues.add(queue);
                messageProducer=session.createProducer(queue);
                messageProducers.add(messageProducer);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private int getMessageProducerId(){
        counter++;
        return counter%numQs;
    }

    public List<Queue> getQueues(){
        return queues;
    }

    public void sendEvent(Event event){
        try {
            eventMessage = session.createObjectMessage(event);
            messageProducer= messageProducers.get(getMessageProducerId());
            messageProducer.send(eventMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
