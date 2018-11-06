package core;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NightRepository {

    private static DataAdapter adapter = new DataAdapter();

    public static List<Night> getNights() {
        return adapter.getNights();
    }

    public static void saveNights(List<Night> nights) {
        adapter.saveNights(nights);
    }

    public static Optional<Night> getNightById(Integer id) {
        return adapter.getNightById(id);
    }

    public static List<Night> getOpenNights() throws IOException {
        return adapter.getNights()
                .stream()
                .filter(n->!n.isComplete())
                .collect(Collectors.toList());
    }

    public static void saveNight(Night night) {
        adapter.saveNight(night);
    }

    public static void setAdapter(DataAdapter adapter) {
        NightRepository.adapter = adapter;
    }
}
