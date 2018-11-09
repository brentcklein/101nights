import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import core.DataException;
import core.Mode;
import core.Night;
import core.Selector;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import rest.RequestHandler;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RequestHandlerTests {

    private RequestHandler requestHandler;
    private Selector selector;

    @BeforeAll
    void getRequestHandler() {
        try {
            selector = new Selector(Mode.TEST);
            requestHandler = new RequestHandler(selector);
        } catch (DataException de) {
            fail(de.getMessage());
        }
    }

    @Test
    void getRandomNight() {
        Gson g = new Gson();
        try {
            assertEquals(
                    "{}",
                    requestHandler.processImpl(Collections.emptyMap()).getBody());

            selector.getRepository().loadNightsFromFile("src/test/test-nights.json");
            selector.setRandomizer(n->0);

            assertEquals(
                    1,
                    (int)g.fromJson(
                            requestHandler.processImpl(Collections.emptyMap()).getBody(),
                            Night.class)
                    .getId()
            );

        } catch (DataException|IOException|JsonSyntaxException|JsonIOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void getNightWithFilters() {
        Gson g = new Gson();
        try {
            selector.getRepository().loadNightsFromFile("src/test/test-nights.json");
            selector.setRandomizer(n->0);

            assertEquals(
                    2,
                    (int)g.fromJson(
                            requestHandler.processImpl(
                                    Collections.singletonMap("filters","{complete:true}")
                            ).getBody(),
                            Night.class)
                            .getId()
            );

            assertEquals(
                    3,
                    (int)g.fromJson(
                            requestHandler.processImpl(
                                    Collections.singletonMap("filters","{complete:true,partner:HIM}")
                            ).getBody(),
                            Night.class)
                            .getId()
            );

        } catch (DataException|IOException|JsonSyntaxException|JsonIOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void badRequest() {
        try {
            requestHandler.processImpl(Collections.singletonMap("filters","malformed"));
            fail("Request was bad.");
        } catch (JsonSyntaxException|DataException ignored) {}
    }

    @AfterAll
    void clearNights() {
        try {
            selector.getRepository().getAdapter().clearNights();
        } catch (DataException de) {
            fail(de.getMessage());
        }
    }
}
