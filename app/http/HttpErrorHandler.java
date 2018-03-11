package http;

import com.typesafe.config.Config;
import play.Environment;
import play.Logger;
import play.api.OptionalSourceMapper;
import play.http.DefaultHttpErrorHandler;
import play.mvc.Http;
import play.mvc.Result;
import play.api.routing.Router;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.concurrent.CompletionStage;

public class HttpErrorHandler extends DefaultHttpErrorHandler {
    @Inject
    public HttpErrorHandler(
            Config config, Environment environment, OptionalSourceMapper sourceMapper, Provider<Router> routes) {
        super(config, environment, sourceMapper, routes);
    }

    @Override
    public CompletionStage<Result> onClientError(Http.RequestHeader request, int statusCode, String message) {
        Logger.error("Client Error:{}, code:{}, detail:{}", request.uri(), statusCode, message);
        return super.onClientError(request, statusCode, message);
    }

    @Override
    public CompletionStage<Result> onServerError(Http.RequestHeader request, Throwable exception) {
        Logger.error("Server Error:{}, ex:{}", request.uri(), exception);
        return super.onServerError(request, exception);
    }
}
