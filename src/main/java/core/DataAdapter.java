package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataAdapter {
    private DataSource source;

    public DataAdapter() {
        source = new DataSource();
    }

    public List<Night> getNights() {
        List<Night> nights = new ArrayList<>(source.getNights().values());

        return nights;
    }

    public void saveNights(List<Night> nights) {
        source.setNights(
                nights.stream().collect(Collectors.toMap(Night::getId,n->n))
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
}
