package pt.utl.ist.notifcenter.domain;

public class GreetingNative {

    private long id;
    private String content;

    /*
    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }*/

    public GreetingNative() {

    }

    public void setId(long id){
        this.id = id;
    }

    public void setContent(String content){
        this.content = content;
    }


    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

}