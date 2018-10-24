import core.DataAdapter;
import core.Night;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;

public class DataAdapterTests {
    /**
     * It should create night nights and return them
     **/
    @Test
    void createNights() throws IOException {
        DataAdapter.saveNights(Arrays.asList(
                new Night(1),
                new Night(2),
                new Night(3),
                new Night(4)
        ));

        assertEquals(4,DataAdapter.getNights().size());
    }

    @Test
    void createNewNight() throws IOException {
        DataAdapter.saveNights(Arrays.asList(
                new Night(1),
                new Night(2),
                new Night(3),
                new Night(4)
        ));

        assertEquals(4,DataAdapter.getNights().size());

        DataAdapter.saveNight(new Night(5));

        assertEquals(5,DataAdapter.getNights().size());
    }

    @Test
    void getNightById() {
        DataAdapter.saveNights(Arrays.asList(
                new Night(1),
                new Night(2),
                new Night(3),
                new Night(4)
        ));

        DataAdapter.getNightById(3)
                .ifPresentOrElse(
                        night->assertEquals(3,(int)night.getId()),
                        ()->fail("Night not found."));
    }

    @Test
    void updateNight() {
        DataAdapter.saveNights(Arrays.asList(
                new Night(1),
                new Night(2),
                new Night(3),
                new Night(4)
        ));

        DataAdapter.getNightById(2)
                .ifPresentOrElse(
                        n -> {
                            n.setComplete(true);
                            DataAdapter.saveNight(n);
                        },
                        ()->fail("Could not get Night.")
                );

        DataAdapter.getNightById(2)
                .ifPresentOrElse(
                        n->assertTrue(n.isComplete()),
                        ()->fail("Could not get Night.")
                );
    }
}
