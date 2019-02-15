package domain;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by Liam on 13/02/2019.
 */
public interface EventBuilder {
    Event build();
    EventBuilder setId(final String id);
    EventBuilder setStart(final String duration);
    EventBuilder setEnd(final String duration);
    EventBuilder setStartEvent(final Event event);

    void setDuration();
    void setAlert();
    JsonNode getJsonNode();
}
