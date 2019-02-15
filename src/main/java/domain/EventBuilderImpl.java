package domain;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Liam on 13/02/2019.
 */
public class EventBuilderImpl implements EventBuilder {

    private JsonNode jsonNode;
    private Event event;

    final static Logger logger = LoggerFactory.getLogger(EventBuilderImpl.class);

    private String getHost(){
        return jsonNode.get("host").asText();
    }

    private String getType(){
        return jsonNode.get("type").asText();
    }


    public EventBuilderImpl(JsonNode jsonNode, Boolean isApplicationServerLogEvent){
        this.jsonNode = jsonNode;

        if (isApplicationServerLogEvent){
            this.event= new ApplicationServerLogEvent(getType(),getHost());
        }else{
            this.event = new Event();
        }
    }

    @Override
    public Event build() {
        return event;
    }


    @Override
    public EventBuilder setId(String id) {
        event.setId(id);
        return this;
    }

    @Override
    public EventBuilder setStart(String timeStamp) {
        event.setStartTime(Long.parseLong(timeStamp));
        return this;
    }

    @Override
    public EventBuilder setEnd(String timeStamp) {
        event.setEndTime(Long.parseLong(timeStamp));
        setDuration();
        return this;
    }

    @Override
    public EventBuilder setStartEvent(Event event) {
        this.event=event;
        return this;
    }

    @Override
    public void setDuration() {
        event.setDuration(event.getEndTime() - event.getStartTime());
        setAlert();
    }

    @Override
    public void setAlert() {
        if (event.getDuration() > 3){
            event.setAlert(true);
            logger.debug("Setting Alert to true!! duration is" + event.getDuration());
        }
    }

    @Override
    public JsonNode getJsonNode() {
        return jsonNode;
    }



}
