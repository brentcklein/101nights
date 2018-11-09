package rest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import core.DataException;
import core.Selector;

import java.util.Map;

public class RequestHandler extends AbstractRequestHandler {
    private Selector selector;

    public RequestHandler(Selector selector) {
        this.selector = selector;
    }

    public Answer processImpl (Map<String,String> queryParams) throws DataException, JsonSyntaxException {
        Gson g = new Gson();

        return Answer.ok(selector.getRandomNight(
                g.<Map<String, Object>>fromJson(
                        queryParams.get("filters"),
                        new TypeToken<Map<String, Object>>() {
                        }.getType()
                )
        )
                .map(g::toJson)
                .orElse("{}"));
    }
}
