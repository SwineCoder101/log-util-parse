package domain;

import java.io.Serializable;

/**
 * Created by Liam on 13/02/2019.
 */
public class Event implements Serializable {
    private String id;
    private long startTime;
    private long endTime;
    private long duration;
    private boolean alert;

    public Event(){
        this.id = "";
        this.startTime=0;
        this.endTime=0;
        this.duration=0;
        this.alert=false;
    }

    public void setId(String Id){
        this.id=Id;
    }

    public void setStartTime(long startTime){
        this.startTime=startTime;
    }

    public void setEndTime(long endTime){
        this.endTime=endTime;
    }

    public void setDuration(long duration){
        this.duration=duration;
    }

    public void setAlert(boolean alert){
        this.alert=alert;
    }

    public String getId(){
        return this.id;
    }

    public long getStartTime(){
        return this.startTime;
    }

    public long getEndTime(){
        return this.endTime;
    }

    public long getDuration(){
        return this.duration;
    }

    public boolean getAlert(){
        return this.alert;
    }



}
