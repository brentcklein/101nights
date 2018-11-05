import core.Night;
import core.PostgresAdapter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostgresAdapterTests {
    private PostgresAdapter adapter;

    @BeforeAll
    void connectToDatabase() {
        try {
            adapter = new PostgresAdapter();
        } catch (SQLException se) {
            fail("Could not connect to the database.");
        }
    }

    @Test
    void createNights() throws IOException {
        adapter.saveNights(Collections.emptyList());

        adapter.saveNights(Arrays.asList(
                new Night(),
                new Night(),
                new Night(),
                new Night()
        ));

        assertEquals(4,adapter.getNights().size());
    }

    @Test
    void getNights() {
        try {
            assertEquals(4,adapter.getNights().size());
        } catch (IOException ioe) {
            fail("Could not get nights.");
        }
    }


}
