package pt.utl.ist.notifcenter.domain;

public class Greeting {

    private long id;
    private String content;

    public Greeting() {

    }

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
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

    @Override
    public String toString() {
        return "object id " + this.getId() + " with content '" + this.getContent() + "'";
    }

}