package core;

public class Night {
    private Integer id;
    private boolean complete;

    public Night(Integer id, boolean complete) {
        this.id = id;
        this.complete = complete;
    }

    public Night(Integer id) {
        this(id,false);
    }

    public Integer getId() {
        return id;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
