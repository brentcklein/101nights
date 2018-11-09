package rest;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;

public abstract class AbstractRequestHandler implements Route {
    protected abstract Answer processImpl(Map<String,String> queryParams) throws Exception;

    public final Answer process(Map<String,String> queryParams) throws Exception {
        return processImpl(queryParams);
    }

    public Object handle(Request request, Response response) throws Exception {
        Answer answer = process(request.params());
        response.status(answer.getCode());
        response.body(answer.getBody());
        return answer.getBody();
    }
}
