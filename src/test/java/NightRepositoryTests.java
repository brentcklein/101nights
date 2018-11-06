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
        this.repository = new NightRepository(Mode.TEST);
    }

    @Test
    void saveNights() {
        repository.saveNights(Arrays.asList(
                new Night(1),
                new Night(2),
                new Night(3),
                new Night(4)
        ));

        List<Night> nights = repository.getNights();

        assertEquals(4,nights.size());
    }

    @Test
    void getNightById() {
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
    }

    @Test
    void updateNight() {
        repository.saveNights(Arrays.asList(
                new Night(1),
                new Night(2),
                new Night(3),
                new Night(4)
        ));

        repository.getNightById(2)
                .ifPresent(
                        n -> {
                            assertFalse(n.isComplete());
                            n.setComplete(true);
                            repository.saveNight(n);
                        }
                );

        repository.getNightById(2)
                .ifPresentOrElse(
                        n->assertTrue(n.isComplete()),
                        ()->fail("Could not get Night.")
                );
    }

    @Test
    void getOpenNights() throws IOException {
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
    }

    @AfterEach
    private void clearNights() {
        repository.saveNights(Collections.emptyList());
    }
}
