import core.Night;
import core.NightRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NightRepositoryTests {
    @Test
    void saveNights() throws IOException {


        NightRepository.saveNights(Arrays.asList(
                new Night(1),
                new Night(2),
                new Night(3),
                new Night(4)
        ));

        List<Night> nights = NightRepository.getNights();

        assertEquals(4,nights.size());
    }

    @Test
    void getNightById() {
        NightRepository.saveNights(Arrays.asList(
                new Night(1),
                new Night(2),
                new Night(3),
                new Night(4)
        ));

        NightRepository.getNightById(3)
                .ifPresentOrElse(
                        night->assertEquals(3,(int)night.getId()),
                        ()->fail("Night not found."));
    }

    @Test
    void updateNight() throws IOException {
        NightRepository.saveNights(Arrays.asList(
                new Night(1),
                new Night(2),
                new Night(3),
                new Night(4)
        ));

        NightRepository.getNightById(2)
                .ifPresent(
                        n -> {
                            assertFalse(n.isComplete());
                            n.setComplete(true);
                            NightRepository.saveNight(n);
                        }
                );

        NightRepository.getNightById(2)
                .ifPresentOrElse(
                        n->assertTrue(n.isComplete()),
                        ()->fail("Could not get Night.")
                );
    }

    @Test
    void getOpenNights() throws IOException {
        NightRepository.saveNights(Arrays.asList(
                new Night(1),
                new Night(2, true),
                new Night(3, true),
                new Night(4)
        ));

        List<Night> nights = NightRepository.getOpenNights();

        assertEquals(2,nights.size());

        assertTrue(nights.stream().allMatch(
                night -> Arrays.asList(1,4).contains(night.getId())
        ));
    }

    @AfterEach
    private void clearNights() {
        NightRepository.saveNights(Collections.emptyList());
    }
}
