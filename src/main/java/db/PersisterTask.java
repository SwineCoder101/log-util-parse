package db;

import domain.ApplicationServerLogEvent;
import domain.Event;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by Liam on 14/02/2019.
 */
public class PersisterTask extends DatabaseInteraction implements Runnable{

    private Event event;
    private StringBuilder sqlQueryBuilder;
    private Connection con = null;
    private Statement stmt = null;

    public PersisterTask(){
    }

    public void build(Event event){
        this.sqlQueryBuilder = new StringBuilder("INSERT INTO ")
                .append(CreateTable.getTableName())
                .append(" VALUES (")
                .append("'" + event.getId() + "',")
                .append(event.getDuration() + ",");

        if (event instanceof ApplicationServerLogEvent){
            ApplicationServerLogEvent appEvent = (ApplicationServerLogEvent) event;
            this.sqlQueryBuilder
                    .append("'" + appEvent.getType() + "',")
                    .append("'" + appEvent.getHost() + "',");
        }else{
            this.sqlQueryBuilder
                    .append("'" + "" + "',")
                    .append("'" + "" + "',");
        }


        this.sqlQueryBuilder
                .append("'" + event.getAlert() + "'")
                .append(");");
        this.event=event;
    }

    @Override
    public void run() {
        exec(sqlQueryBuilder.toString());
    }
}
