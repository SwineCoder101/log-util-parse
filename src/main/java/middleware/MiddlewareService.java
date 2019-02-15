package middleware;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.jms.ConnectionFactory;
import javax.jms.Connection;
import javax.jms.Session;
import java.net.URI;

/**
 * Created by Liam on 14/02/2019.
 */


public class MiddlewareService {

    protected BrokerService broker;
    protected ConnectionFactory connectionFactory;
    private Session session;
    private Connection connection;
    final static Logger logger = LoggerFactory.getLogger(MiddlewareService.class);


    // TODO set transport.useInactivityMonitor=false
    // TODO segregate localhost url
    public MiddlewareService(){
        try {
            broker = BrokerFactory.createBroker(new URI("broker:(tcp://localhost:61619)"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Created Middleware Service on localhost:61619");
    }

    public void start(){
        try {
            logger.info("Starting Middleware Service");
            broker.start();
            connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61619");
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            session.close();
            broker.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Session getSession(){
        return session;
    }

    public Connection getConnection(){
        return connection;
    }

}
