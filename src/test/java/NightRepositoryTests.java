import core.DataException;
import core.Mode;
import core.Night;
import core.NightRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NightRepositoryTests {
    private NightRepository repository;

    @BeforeAll
    void createRepository() {
        try {
            this.repository = new NightRepository(Mode.TEST);
        } catch (DataException de) {
            fail("Could not connect to database.");
        }
    }

    @Test
    void saveNights() {
        try {
            repository.saveNights(Arrays.asList(
                    new Night(1),
                    new Night(2),
                    new Night(3),
                    new Night(4)
            ));

            List<Night> nights = repository.getNights();

            assertEquals(4,nights.size());
        } catch (DataException de) {
            fail("DataException thrown.");
        }
    }

    @Test
    void getNightById() {
        try {
            repository.saveNights(Arrays.asList(
                    new Night(1),
                    new Night(2),
                    new Night(3),
                    new Night(4)
            ));

            repository.getNightById(3)
                    .ifPresentOrElse(
                            night->assertEquals(3,(int)night.getId()),
                            ()->fail("Night not found."));
        } catch (DataException de) {
            fail("DataException thrown.");
        }
    }

    @Test
    void updateNight() {
        try {
            repository.saveNights(Arrays.asList(
                    new Night(1),
                    new Night(2),
                    new Night(3),
                    new Night(4)
            ));

            repository.getNightById(2)
                    .ifPresent(
                            n -> {
                                try {
                                    assertFalse(n.isComplete());
                                    n.setComplete(true);
                                    repository.saveNight(n);
                                } catch (DataException de) {
                                    fail("DataException thrown.");
                                }
                            }
                    );

            repository.getNightById(2)
                    .ifPresentOrElse(
                            n->assertTrue(n.isComplete()),
                            ()->fail("Could not get Night.")
                    );
        } catch (DataException de) {
            fail("DataException thrown.");
        }
    }

    @Test
    void getOpenNights() {
        try {
            repository.saveNights(Arrays.asList(
                    new Night(1),
                    new Night(2, true),
                    new Night(3, true),
                    new Night(4)
            ));

            List<Night> nights = repository.getOpenNights();

            assertEquals(2,nights.size());

            assertTrue(nights.stream().allMatch(
                    night -> Arrays.asList(1,4).contains(night.getId())
            ));
        } catch (DataException de) {
            fail("DataException thrown.");
        }
    }

    @AfterEach
    private void clearNights() {
        try {
            repository.saveNights(Collections.emptyList());
        } catch (DataException de) {
            fail("DataException thrown.");
        }
    }

    @AfterEach
    void clearDatabase() {
        try {
            repository.getAdapter().clearNights();
        } catch (DataException de) {
            fail("Could not clear nights: " + de.getMessage());
        }
    }
}
