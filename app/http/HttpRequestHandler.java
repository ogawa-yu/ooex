package http;

import com.google.inject.Inject;
import play.Logger;
import play.api.http.JavaCompatibleHttpRequestHandler;
import play.http.DefaultHttpRequestHandler;
import play.http.HandlerForRequest;
import play.mvc.Http;

public class HttpRequestHandler extends DefaultHttpRequestHandler {

    @Inject
    public HttpRequestHandler(JavaCompatibleHttpRequestHandler underlying) {
        super(underlying);
    }

    @Override
    public HandlerForRequest handlerForRequest(Http.RequestHeader request) {
        Logger.trace("Request: {}", request.uri());
        return super.handlerForRequest(request);
    }
}
