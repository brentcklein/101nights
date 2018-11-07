package core;

import static spark.Spark.*;

public class SelectorController {
    public static void main(String[] args) {
        port(4567);
        get("/hello", (request, response) -> "Hello!");
    }
}
