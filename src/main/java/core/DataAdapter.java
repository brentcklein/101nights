package core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataAdapter {
    private static DataSource source = new DataSource();

    public static List<Night> getNights() throws IOException {
        List<Night> nights = new ArrayList<>(source.getNights().values());
        if (nights.size() == 0) {
            throw new IOException("Could not get list of Nights.");
        }

        return nights;
    }

    public static void saveNights(List<Night> nights) {
        source.setNights(
                nights.stream().collect(Collectors.toMap(Night::getId,n->n))
        );
    }

    public static Optional<Night> getNightById(Integer id) {
        return source
                .getNights()
                .values()
                .stream()
                .filter(n -> {
                    return n.getId().equals(id);
                })
                .findFirst();
    }

    public static void saveNight(Night night) {
        source.setNight(night.getId(),night);
    }
}
