package rest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import core.DataException;
import core.Selector;

import java.util.Map;

import static spark.Spark.*;

public class SelectorController {
    private static Selector selector;

    public static void main(String[] args) {
        try {
            SelectorController.selector = new Selector();
        } catch (DataException de) {
            System.out.println(de.getMessage());
            System.exit(1);
        }

        Gson g = new Gson();

        port(4567);
        get("/random", (request, response) ->
            selector.getRandomNight(
                    g.<Map<String,Object>>fromJson(
                            request.queryParams("filters"),
                            new TypeToken<Map<String,Object>>(){}.getType()
                    )
            )
                    .map(g::toJson)
                    .orElse("{}")
        );

        exception(JsonSyntaxException.class, (e, request, response) -> {
            response.status(400);
            response.body("Invalid JSON syntax.");
        });
    }
}
