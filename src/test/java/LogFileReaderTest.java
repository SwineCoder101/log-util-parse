import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import db.TestHelper;
import domain.EventBuilderDirector;
import middleware.EventProducers;
import middleware.MiddlewareService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by Liam on 13/02/2019.
 */

public class LogFileReaderTest extends TestHelper {

    LogFileReader existentLogFileReader;
    LogFileReader nonExistentLogFileReader;
    MiddlewareService middlewareService;
    EventProducers eventProducers;
    EventBuilderDirector eventBuilderDirector;


    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Before
    public void init(){
        eventBuilderDirector = new EventBuilderDirector(middlewareService,eventProducers);
        existentLogFileReader = new LogFileReader("example_log.txt",eventBuilderDirector);
    }

    @Test
    public void shouldReadFileCorrectly(){
        //Given log file with 6 lines in init
        int count =0;
        JsonNode jsonNode = null;

        //Then read all 6 lines within file and parse without exceptioning
        while (existentLogFileReader.getScanner().hasNext()){
            jsonNode= existentLogFileReader.getJsonNode(existentLogFileReader.getScanner().nextLine());
            count++;
        }
        assertThat(count,equalTo(6));
        assertThat(trim(jsonNode.get("id")),equalTo("scsmbstgrb"));
    }

    @Test
    public void shouldThrowFileNotFoundException(){
        //Given non-existent log file
        nonExistentLogFileReader=new LogFileReader("non_existent.txt",eventBuilderDirector);

        //Then throw file not found exception
        thrown.equals(FileNotFoundException.class);
    }

    @Test
    public void shouldParseStringLoggerToJsonNodeCorrectly(){
        //Given valid logger
        String validLogger = "{\"id\":\"scsmbstgrb\", \"state\":\"STARTED\", \"timestamp\":1491377495213}";

        //Then parse correctly
        JsonNode jsonNode = existentLogFileReader.getJsonNode(validLogger);

        assertThat(trim(jsonNode.get("id")),equalTo("scsmbstgrb"));
        assertThat(trim(jsonNode.get("state")),equalTo("STARTED"));
        assertThat(trim(jsonNode.get("timestamp")),equalTo("1491377495213"));
    }

    @Test
    public void shouldThrowParseExceptionWhenLoggerFormatIsWrong(){
        //Given an invalid json logger
        String validLogger = "{\"id\":\"scsmbstgrb\", \"state\":\"STARTED\", \"timestamp\":1491377495213{}}}";

        //Then throw parse exception
        JsonNode jsonNode = existentLogFileReader.getJsonNode(validLogger);
        thrown.equals(JsonParseException.class);
    }
}
