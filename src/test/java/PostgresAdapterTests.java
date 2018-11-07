import core.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostgresAdapterTests {
    private PostgresAdapter adapter;

    @BeforeAll
    void connectToDatabase() {
        try {
            adapter = new PostgresAdapter(Mode.TEST);
        } catch (DataException se) {
            fail("Could not connect to the database.");
        }
    }

    @Test
    void createAndGetNights() {
        assertEquals(0,adapter.getNights().size());

        adapter.saveNights(Arrays.asList(
                new Night(),
                new Night(),
                new Night(),
                new Night()
        ));

        assertEquals(4,adapter.getNights().size());

    }

    @Test
    void createAndGetNight() {
        adapter.getNightById(1)
                .ifPresent(night -> fail("Night should not exist."));

        adapter.saveNight(new Night());

        List<Night> nights = adapter.getNights();
        assertEquals(1,nights.size());

        adapter.getNightById(nights.get(0).getId())
                .ifPresentOrElse(
                        night -> assertEquals(nights.get(0).getId(),night.getId()),
                        ()->fail("Night not found."));
    }

    @Test
    void updateNight() {
        adapter.saveNight(new Night(1));

        adapter.getNightById(1)
        .flatMap(
                night -> {
                    assertFalse(night.isComplete());
                    night.setComplete(true);
                    adapter.saveNight(night);
                    return adapter.getNightById(1);

                })
        .ifPresentOrElse(
                night -> assertTrue(night.isComplete()),
                () -> fail("Could not get Night.")
        );
    }

    @Test
    void updateNights() {
        adapter.saveNights(Arrays.asList(
                new Night(1),
                new Night(2),
                new Night(3),
                new Night(4)
        ));

        List<Night> nights = adapter.getNights();

        nights.get(0).setComplete(true);
        nights.get(1).setCost(Cost.HIGH);
        nights.get(2).setHasECard(true);
        nights.get(3).setInvolvesTravel(true);

        adapter.saveNights(nights);

        nights = adapter.getNights();

        assertTrue(nights.get(0).isComplete());
        assertEquals(Cost.HIGH, nights.get(1).getCost());
        assertTrue(nights.get(2).hasECard());
        assertTrue(nights.get(3).involvesTravel());
    }

    @AfterEach
    void clearDatabase() {
        try {
            adapter.clearNights();
        } catch (SQLException se) {
            fail("Could not clear nights.");
        }
    }
}
