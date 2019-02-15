import com.fasterxml.jackson.databind.JsonNode;
import db.PersisterService;
import domain.EventBuilderDirector;
import middleware.EventConsumers;
import middleware.EventProducers;
import middleware.MiddlewareService;

import java.util.Scanner;

/**
 * Created by Liam on 13/02/2019.
 */
public class LogUtilMain {

    public static void main(String[] args){

        String filepath;// = args[0];
        filepath="example_log.txt";

        // Set up Middleware
        MiddlewareService middlewareService = new MiddlewareService();
        middlewareService.start();
        EventProducers eventProducers=new EventProducers(4,middlewareService);
        EventConsumers eventConsumers = new EventConsumers(eventProducers.getQueues(),middlewareService);

        //Set up Persistence
        PersisterService persisterService = new PersisterService(eventConsumers);

        //Set up log Reader
        EventBuilderDirector eventBuilderDirector = new EventBuilderDirector(middlewareService,eventProducers);
        LogFileReader logFileReader = new LogFileReader(filepath,eventBuilderDirector);
        Thread logFileReaderThread = new Thread(logFileReader);

        //run logfile reader
        logFileReaderThread.run();

        //run persistence and middleware in parallel
        persisterService.run();


    }
}
