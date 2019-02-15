package middleware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.management.snmp.util.MibLogger;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import java.util.ArrayList;
import java.util.List;
import javax.jms.Queue;

/**
 * Created by Liam on 14/02/2019.
 */

public class EventConsumers{

    private List<Queue> queues;
    private List<MessageConsumer> messageConsumers;
    private Session session;
    private MiddlewareService middlewareService;

    final static Logger logger = LoggerFactory.getLogger(EventConsumers.class);

    public EventConsumers(List<Queue> queues, MiddlewareService middlewareService) {
        this.queues = queues;
        this.messageConsumers = new ArrayList<>();
        this.middlewareService=middlewareService;
        session = middlewareService.getSession();


    }

    public void listen(){
        MessageConsumer messageConsumer;

        logger.info("Creating " + queues.size() + " event consumers with listeners");
        try {
            for (int i = 0; i < queues.size(); i++) {
                Queue queue = queues.get(i);
                messageConsumer = session.createConsumer(queue);
                messageConsumer.setMessageListener(new EventListener());
                messageConsumers.add(messageConsumer);
            }
            middlewareService.getConnection().start();

        }catch(JMSException je){
            je.printStackTrace();
        }
    }
}
