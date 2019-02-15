import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.EventBuilderDirector;
import middleware.EventConsumers;
import middleware.EventProducers;
import middleware.MiddlewareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Liam on 13/02/2019.
 */
public class LogFileReader implements Runnable{
    private String logPath;
    private FileInputStream inputStream;
    private Scanner scanner;
    private ObjectMapper mapper;
    private EventBuilderDirector eventBuilderDirector;


    final static Logger logger = LoggerFactory.getLogger(LogFileReader.class);

    LogFileReader(String logPath, EventBuilderDirector eventBuilderDirector){
        this.logPath=logPath;
        this.mapper = new ObjectMapper();
        this.eventBuilderDirector=eventBuilderDirector;

        try {
            inputStream = new FileInputStream(logPath);
            scanner = new Scanner(inputStream, "UTF-8");

            logger.info("Successfully read log file " + logPath);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public JsonNode getJsonNode(String logger){
        JsonNode jsonNode = null;

        try{
            jsonNode=  mapper.readTree(logger);
        }catch(JsonParseException jsonParseException){
            jsonParseException.printStackTrace();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }

        return jsonNode;
    }

    public Scanner getScanner(){
        return scanner;
    }


    @Override
    public void run() {
        JsonNode jsonNode;

        logger.info("Running file Log Parser..");

        while(this.scanner.hasNextLine()){
            jsonNode= getJsonNode(this.scanner.nextLine());
            eventBuilderDirector.construct(jsonNode);
        }

    }

}
