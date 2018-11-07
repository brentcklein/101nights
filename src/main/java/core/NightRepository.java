package core;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NightRepository {

    private DataAdapter adapter;

    public NightRepository(Mode mode) throws DataException {
        this.setAdapter(new MockAdapter(mode));
    }

    public NightRepository() throws DataException {
        this(Mode.DEV);
    }

    public List<Night> getNights() {
        return adapter.getNights();
    }

    public void saveNights(List<Night> nights) {
        adapter.saveNights(nights);
    }

    public Optional<Night> getNightById(Integer id) {
        return adapter.getNightById(id);
    }

    public List<Night> getOpenNights() {
        return adapter.getNights()
                .stream()
                .filter(n->!n.isComplete())
                .collect(Collectors.toList());
    }

    public void saveNight(Night night) {
        adapter.saveNight(night);
    }

    public void setAdapter(DataAdapter adapter) {
        this.adapter = adapter;
    }
}
