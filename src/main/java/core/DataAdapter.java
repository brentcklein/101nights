package core;

import java.util.List;
import java.util.Optional;

public abstract class DataAdapter {
    protected Mode mode;
    public DataAdapter(Mode mode) throws DataException {
        this.mode = mode;
    }

    public abstract List<Night> getNights();
    public abstract Optional<Night> getNightById(Integer id);
    public abstract void saveNights(List<Night> nights);
    public abstract void saveNight(Night night);
}
