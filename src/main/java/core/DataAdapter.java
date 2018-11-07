package core;

import java.util.List;
import java.util.Optional;

public abstract class DataAdapter {
    protected Mode mode;
    public DataAdapter(Mode mode) throws DataException {
        this.mode = mode;
    }

    public abstract List<Night> getNights() throws DataException;
    public abstract Optional<Night> getNightById(Integer id) throws DataException;
    public abstract void saveNights(List<Night> nights) throws DataException;
    public abstract void saveNight(Night night) throws DataException;
    public abstract void clearNights() throws DataException;
}
