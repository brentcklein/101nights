package rest;

import com.google.gson.JsonSyntaxException;
import core.DataException;
import core.Selector;

import static spark.Spark.*;

public class SelectorController {

    public static void main(String[] args) {
        try {
            port(4567);
            get("/random", new RequestHandler(new Selector()));

            exception(JsonSyntaxException.class, (e, request, response) -> {
                response.status(400);
                response.body("Invalid JSON syntax.");
            });

            exception(DataException.class, (e, request, response) -> {
                response.status(500);
                response.body("Error processing request: " + e.getMessage());
            });
        } catch (DataException de) {
            System.out.println(de.getMessage());
            System.exit(1);
        }
    }
}
