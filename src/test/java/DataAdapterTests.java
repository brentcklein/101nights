import core.DataAdapter;
import core.Night;
import core.NightRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class DataAdapterTests {
    /**
     * It should create night nights and return them
     **/
    @Test
    void createNights() throws IOException {
        DataAdapter adapter = new DataAdapter();

        adapter.saveNights(Collections.emptyList());

        adapter.saveNights(Arrays.asList(
                new Night(1),
                new Night(2),
                new Night(3),
                new Night(4)
        ));

        assertEquals(4,adapter.getNights().size());
    }

    @Test
    void createNewNight() throws IOException {
        DataAdapter adapter = new DataAdapter();

        adapter.saveNights(Arrays.asList(
                new Night(1),
                new Night(2),
                new Night(3),
                new Night(4)
        ));

        assertEquals(4,adapter.getNights().size());

        adapter.saveNight(new Night(5));

        assertEquals(5,adapter.getNights().size());
    }

    @Test
    void getNightById() {
        DataAdapter adapter = new DataAdapter();

        adapter.saveNights(Arrays.asList(
                new Night(1),
                new Night(2),
                new Night(3),
                new Night(4)
        ));

        adapter.getNightById(3)
                .ifPresentOrElse(
                        night->assertEquals(3,(int)night.getId()),
                        ()->fail("Night not found."));
    }

    @Test
    void updateNight() throws IOException {
        DataAdapter adapter = new DataAdapter();

        adapter.saveNights(Arrays.asList(
                new Night(1),
                new Night(2),
                new Night(3),
                new Night(4)
        ));

        adapter.getNightById(2)
                .ifPresentOrElse(
                        n -> {
                            n.setComplete(true);
                            adapter.saveNight(n);
                        },
                        ()->fail("Could not get Night.")
                );

        adapter.getNightById(2)
                .ifPresentOrElse(
                        n->assertTrue(n.isComplete()),
                        ()->fail("Could not get Night.")
                );
    }

//    @AfterEach
//    private void clearNights() {
//        NightRepository.saveNights(Collections.emptyList());
//    }
}
