import com.fasterxml.jackson.databind.JsonNode;
import domain.ApplicationServerLogEvent;
import domain.Event;
import domain.EventBuilderDirector;
import middleware.EventProducers;
import middleware.MiddlewareService;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

/**
 * Created by Liam on 13/02/2019.
 */
public class EventBuilderDirectorTest {

    LogFileReader logFileReader;
    EventBuilderDirector eventBuilderDirector;
    final static String idStateStart="{\"id\":\"scsmbstgrb\", \"state\":\"STARTED\",";
    final static String idStateEnd="{\"id\":\"scsmbstgrb\", \"state\":\"FINISHED\",";

    final static String applicationlog="\"type\":\"APPLICATION_LOG\",\"host\":\"12345\",";

    final static String baseTimeStamp ="\"timestamp\":1491377495213}";
    final static String baseTimeStampPlusOne ="\"timestamp\":1491377495214}";
    final static String baseTimeStampPlusSix ="\"timestamp\":1491377495219}";

    final static String loggerBasicStartEvent = idStateStart + baseTimeStamp;
    final static String loggerBasicEndEventPlusOne = idStateEnd + baseTimeStampPlusOne;
    final static String loggerBasicEndEventPlusSix  = idStateEnd + baseTimeStampPlusSix;

    final static String loggerAppStartEvent = idStateStart + applicationlog + baseTimeStamp;
    final static String loggerAppEndEventPlusOne = idStateEnd + applicationlog + baseTimeStampPlusOne;
    final static String loggerAppEndEventPlusSix  = idStateEnd + applicationlog + baseTimeStampPlusSix;
    MiddlewareService middlewareService;
    EventProducers eventProducers;


    @Before
    public void init() {
        eventBuilderDirector = new EventBuilderDirector(middlewareService,eventProducers);
        logFileReader = new LogFileReader("example_log.txt",eventBuilderDirector);

        middlewareService = new MiddlewareService();
        eventProducers = new EventProducers(2,middlewareService);

    }

    @Test
    public void shouldParseNormalLogEventWithAlertFalse(){
        //Given normal start and end event logger
        String loggerStartEvent = loggerBasicStartEvent;
        String loggerEndEvent = loggerBasicEndEventPlusOne;

        //When constructing the events
        JsonNode jsonNodeStart = logFileReader.getJsonNode(loggerStartEvent);
        JsonNode jsonNodeEnd = logFileReader.getJsonNode(loggerEndEvent);
        eventBuilderDirector.construct(jsonNodeStart);
        eventBuilderDirector.construct(jsonNodeEnd);

        //Then normal event created successfully with alert
        Event event = eventBuilderDirector.getEvent();
        assertThat(event.getId(),equalTo("scsmbstgrb"));
        assertThat(event.getAlert(),is(false));
        assertThat(event.getDuration(),equalTo(1L));
        assertThat(eventBuilderDirector.getEventCacheMap().containsKey("scsmbstgrb"), is(false));
    }

    @Test
    public void shouldParseNormalLogEventWithAlertTrue(){
        //Given normal start and end event logger
        String loggerStartEvent = loggerBasicStartEvent;
        String loggerEndEvent = loggerBasicEndEventPlusSix;

        //When constructing the events
        JsonNode jsonNodeStart = logFileReader.getJsonNode(loggerStartEvent);
        JsonNode jsonNodeEnd = logFileReader.getJsonNode(loggerEndEvent);
        eventBuilderDirector.construct(jsonNodeStart);
        eventBuilderDirector.construct(jsonNodeEnd);

        //Then normal event created successfully without alert
        Event event = eventBuilderDirector.getEvent();
        assertThat(event.getId(),equalTo("scsmbstgrb"));
        assertThat(event.getAlert(),is(true));
        assertThat(event.getDuration(),equalTo(6L));
        assertThat(eventBuilderDirector.getEventCacheMap().containsKey("scsmbstgrb"), is(false));
    }

    @Test
    public void shouldParseApplicationLogEventWithAlertFalse(){
        //Given application event logger
        String loggerStartEvent = loggerAppStartEvent;
        String loggerEndEvent = loggerAppEndEventPlusOne;

        //When constructing the events
        JsonNode jsonNodeStart = logFileReader.getJsonNode(loggerStartEvent);
        JsonNode jsonNodeEnd = logFileReader.getJsonNode(loggerEndEvent);
        eventBuilderDirector.construct(jsonNodeStart);
        eventBuilderDirector.construct(jsonNodeEnd);

        //Then application event created successfully
        ApplicationServerLogEvent event = (ApplicationServerLogEvent) eventBuilderDirector.getEvent();
        assertThat(event.getId(),equalTo("scsmbstgrb"));
        assertThat(event.getHost(), equalTo("12345"));
        assertThat(event.getType(), equalTo("APPLICATION_LOG"));
        assertThat(event.getAlert(),is(false));
        assertThat(event.getDuration(),equalTo(1L));
        assertThat(eventBuilderDirector.getEventCacheMap().containsKey("scsmbstgrb"), is(false));
    }

    @Test
    public void shouldParseApplicationLogEventWithAlertTrue(){
        //Given application event logger
        String loggerStartEvent = loggerAppStartEvent;
        String loggerEndEvent = loggerAppEndEventPlusSix;

        //When constructing the events
        JsonNode jsonNodeStart = logFileReader.getJsonNode(loggerStartEvent);
        JsonNode jsonNodeEnd = logFileReader.getJsonNode(loggerEndEvent);
        eventBuilderDirector.construct(jsonNodeStart);
        eventBuilderDirector.construct(jsonNodeEnd);

        //Then application event created successfully
        ApplicationServerLogEvent event = (ApplicationServerLogEvent) eventBuilderDirector.getEvent();
        assertThat(event.getId(),equalTo("scsmbstgrb"));
        assertThat(event.getHost(), equalTo("12345"));
        assertThat(event.getType(), equalTo("APPLICATION_LOG"));
        assertThat(event.getAlert(),is(true));
        assertThat(event.getDuration(),equalTo(6L));
        assertThat(eventBuilderDirector.getEventCacheMap().containsKey("scsmbstgrb"), is(false));
    }


    @Test
    public void shouldThrowParseExceptionWhenMissingFieldLogEvent(){
        //Given a log event with null id

        //Then application event created successfully
    }

    @Test
    public void shouldThrowParseExceptionWhenMissingFieldApplicationLogEvent(){
        //Given a log event with null

        //Then application event created successfully
    }


}
