import core.DataAdapter;
import core.DataException;
import core.MockAdapter;
import core.Night;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MockAdapterTests {
    private DataAdapter adapter;

    @BeforeAll
    void getAdapter() {
        try {
            adapter = new MockAdapter();
        } catch (DataException de) {
            fail("Could not connect to database.");
        }
    }

    /**
     * It should create night nights and return them
     **/
    @Test
    void createNights() {
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
    void createNewNight() {
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
    void updateNight() {
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
