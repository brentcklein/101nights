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
        try {
            adapter.saveNights(Collections.emptyList());

            adapter.saveNights(Arrays.asList(
                    new Night(1),
                    new Night(2),
                    new Night(3),
                    new Night(4)
            ));

            assertEquals(4,adapter.getNights().size());
        } catch (DataException de) {
            fail("Could not create Nights.");
        }
    }

    @Test
    void createNewNight() {
        try {
            adapter.saveNights(Arrays.asList(
                    new Night(1),
                    new Night(2),
                    new Night(3),
                    new Night(4)
            ));

            assertEquals(4,adapter.getNights().size());

            adapter.saveNight(new Night(5));

            assertEquals(5,adapter.getNights().size());
        } catch (DataException de) {
            fail("Could not create Night.");
        }
    }

    @Test
    void getNightById() {
        try {
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
        } catch (DataException de) {
            fail("Could not get Night.");
        }
    }

    @Test
    void updateNight() {
        try {
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
                                try {
                                    adapter.saveNight(n);
                                } catch (DataException de) {
                                    fail("Could not save Night.");
                                }
                            },
                            ()->fail("Could not get Night.")
                    );

            adapter.getNightById(2)
                    .ifPresentOrElse(
                            n->assertTrue(n.isComplete()),
                            ()->fail("Could not get Night.")
                    );
        } catch (DataException de) {
            fail("Could not .");
        }
    }

//    @AfterEach
//    private void clearNights() {
//        NightRepository.saveNights(Collections.emptyList());
//    }
}
