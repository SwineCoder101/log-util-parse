package domain;

/**
 * Created by Liam on 13/02/2019.
 */
public class ApplicationServerLogEvent extends Event {
    private String type;
    private String host;

    public ApplicationServerLogEvent(String type, String host){
        this.type=type;
        this.host=host;
    }

    public void setHost(String host){
        this.host=host;
    }

    public void setType(String type){
        this.type=type;
    }

    public String getHost(){
        return this.host;
    }

    public String getType(){
        return  this.type;
    }

}
