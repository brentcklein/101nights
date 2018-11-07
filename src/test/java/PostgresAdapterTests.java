import core.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        try {
            assertEquals(0,adapter.getNights().size());

            adapter.saveNights(Arrays.asList(
                    new Night(),
                    new Night(),
                    new Night(),
                    new Night()
            ));

            assertEquals(4,adapter.getNights().size());
        } catch (DataException de) {
            fail("Could not create and get Nights.");
        }
    }

    @Test
    void createAndGetNight() {
        try {
            adapter.getNightById(1)
                    .ifPresent(night -> fail("Night should not exist."));

            adapter.saveNight(new Night());

            List<Night> nights = adapter.getNights();
            assertEquals(1,nights.size());

            adapter.getNightById(nights.get(0).getId())
                    .ifPresentOrElse(
                            night -> assertEquals(nights.get(0).getId(),night.getId()),
                            ()->fail("Night not found."));
        } catch (DataException de) {
            fail("Could not create and get Night.");
        }
    }

    @Test
    void updateNight() {
        try {
            adapter.saveNight(new Night(1));

            adapter.getNightById(1)
                    .flatMap(
                            night -> {
                                try {
                                    assertFalse(night.isComplete());
                                    night.setComplete(true);
                                    adapter.saveNight(night);
                                    return adapter.getNightById(1);
                                } catch (DataException de) {
                                    fail("Could not save and get Night.");
                                }
                                return Optional.empty();
                            })
                    .ifPresentOrElse(
                            night -> assertTrue(night.isComplete()),
                            () -> fail("Could not get Night.")
                    );
        } catch (DataException de) {
            fail("Could not .");
        }
    }

    @Test
    void updateNights() {
        try {
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
        } catch (DataException de) {
            fail("Could not .");
        }
    }

    @AfterEach
    void clearDatabase() {
        try {
            adapter.clearNights();
        } catch (DataException de) {
            fail("Could not clear nights: " + de.getMessage());
        }
    }
}
