package core;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NightRepository {

    private DataAdapter adapter;

    public NightRepository(Mode mode) throws DataException {
        this.setAdapter(new PostgresAdapter(mode));
    }

    public NightRepository() throws DataException {
        this(Mode.DEV);
    }

    public List<Night> getNights() throws DataException {
        return adapter.getNights();
    }

    public void saveNights(List<Night> nights) throws DataException {
        adapter.saveNights(nights);
    }

    public Optional<Night> getNightById(Integer id) throws DataException {
        return adapter.getNightById(id);
    }

    public List<Night> getOpenNights() throws DataException {
        return adapter.getNights()
                .stream()
                .filter(n->!n.isComplete())
                .collect(Collectors.toList());
    }

    public void saveNight(Night night) throws DataException {
        adapter.saveNight(night);
    }

    public void loadNightsFromFile(String path) throws IOException, JsonIOException, JsonSyntaxException, DataException {
        FileReader fr = new FileReader(new File(path));

        Gson g = new Gson();

        ArrayList<Night> nights = g.fromJson(fr,new TypeToken<List<Night>>(){}.getType());

        saveNights(nights);
    }

    public void setAdapter(DataAdapter adapter) {
        this.adapter = adapter;
    }

    public DataAdapter getAdapter() {
        return adapter;
    }
}
