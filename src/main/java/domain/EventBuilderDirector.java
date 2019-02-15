package domain;

import com.fasterxml.jackson.databind.JsonNode;
import middleware.EventProducers;
import middleware.MiddlewareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by Liam on 13/02/2019.
 */

public class EventBuilderDirector {
    private EventBuilder eventBuilder;
    private Map<String,Event> eventCacheMap;
    private EventProducers eventProducers;
    private MiddlewareService middlewareService;

    private Event event;

    final static Logger logger = LoggerFactory.getLogger(EventBuilderDirector.class);

    public EventBuilderDirector(MiddlewareService middlewareService, EventProducers eventProducers){
        eventCacheMap = new HashMap<>();
        this.middlewareService = middlewareService;
        this.eventProducers = eventProducers;
    }

    private boolean isApplicationServerLogEvent(JsonNode jsonNode){
        return jsonNode.has("host") || jsonNode.has("type");
    }

    private boolean isStartEvent(JsonNode jsonNode){
        return(jsonNode.get("state").asText().equals("STARTED"));
    }

    private boolean isEndEvent(JsonNode jsonNode){
        return(jsonNode.get("state").asText().equals("FINISHED"));
    }



    // TODO add Exception Handling
    public boolean construct(JsonNode jsonNode){
        Boolean isAppLogFlag=isApplicationServerLogEvent(jsonNode);
        this.eventBuilder= new EventBuilderImpl(jsonNode, isAppLogFlag);
        String timeStampStr= jsonNode.get("timestamp").asText();
        String idStr= jsonNode.get("id").asText();

        eventBuilder.setId(idStr);

        if (isEndEvent(jsonNode)){
            logger.info("End event for: " + idStr);
            eventBuilder.setStartEvent(eventCacheMap.get(idStr)).setEnd(timeStampStr);


            evictEventFromCacheMapAndSendToQueue(idStr);
        }

        if(isStartEvent(jsonNode)){
            eventBuilder.setStart(timeStampStr);
            eventCacheMap.put(idStr,eventBuilder.build());
        }

        return false;
    }

    public Map<String,Event> getEventCacheMap(){
        return eventCacheMap;
    }

    public void evictEventFromCacheMapAndSendToQueue(String id){
        event = eventCacheMap.get(id);
        eventProducers.sendEvent(event);
        eventCacheMap.remove(id);
    }

    public Event getEvent(){
        return this.event;
    }

}
