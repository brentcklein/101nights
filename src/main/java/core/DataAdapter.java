package core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataAdapter {
    private DataSource source;

    public DataAdapter() {
        source = new DataSource();
    }

    public List<Night> getNights() throws IOException {
        List<Night> nights = new ArrayList<>(source.getNights().values());
        if (nights.size() == 0) {
            throw new IOException("Could not get list of Nights.");
        }

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
