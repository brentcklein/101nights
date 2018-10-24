package core;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NightRepository {

    public static List<Night> getNights() throws IOException {
        return DataAdapter.getNights();
    }

    public static void saveNights(List<Night> nights) {
        DataAdapter.saveNights(nights);
    }

    public static Optional<Night> getNightById(Integer id) {
        return DataAdapter.getNightById(id);
    }

    public static List<Night> getOpenNights() throws IOException {
        return DataAdapter.getNights()
                .stream()
                .filter(n->!n.isComplete())
                .collect(Collectors.toList());
    }

    public static void saveNight(Night night) {
        DataAdapter.saveNight(night);
    }
}
