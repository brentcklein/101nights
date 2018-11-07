package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MockAdapter extends DataAdapter{
    private DataSource source;

    public MockAdapter(Mode mode) throws DataException {
        super(mode);
        source = new DataSource();
    }

    public MockAdapter() throws DataException {
        this(Mode.DEV);
    }

    public List<Night> getNights() {
        return new ArrayList<>(source.getNights().values());
    }

    public void saveNights(List<Night> nights) {
        source.setNights(
                nights.stream().collect(Collectors.toMap(night -> {
                    if (night.getId() == null) {
                        return source.getNextId();
                    }
                    return night.getId();
                }, Function.identity()))
        );
    }

    public Optional<Night> getNightById(Integer id) {
        return source
                .getNights()
                .values()
                .stream()
                .filter(n -> n.getId().equals(id))
                .findFirst();
    }

    public void saveNight(Night night) {
        source.setNight(night.getId(),night);
    }

    public void setSource(DataSource source) {
        this.source = source;
    }

    public void clearNights() { saveNights(Collections.emptyList()); }
}
