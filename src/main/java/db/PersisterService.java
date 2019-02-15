package db;

import middleware.EventConsumers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Liam on 14/02/2019.
 */
public class PersisterService{

    private EventConsumers eventConsumers;
    private DatabaseApplicationServer databaseApplicationServer;

    final static Logger logger = LoggerFactory.getLogger(PersisterService.class);

    public PersisterService(EventConsumers eventConsumers){
        this.eventConsumers = eventConsumers;
        this.databaseApplicationServer = new DatabaseApplicationServer();
    }

    public void run() {
        logger.info("Starting hsqldb server and event listeners");
        databaseApplicationServer.start();
        eventConsumers.listen();
    }
}
