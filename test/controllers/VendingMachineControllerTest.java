package controllers;

import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;

import static org.junit.Assert.*;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class VendingMachineControllerTest extends WithApplication {

    @Test
    public void test_all_drinks() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/vending/drink");

        Result result = route(app, request);
        assertEquals(OK, result.status());
        assertEquals("[0,1,2]", Helpers.contentAsString(result));
    }

    @Test
    public void test_pay() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/vending/money/100");

        Result result = route(app, request);
        assertEquals(OK, result.status());
        assertEquals("{\"kinds\":[\"COKE\",\"DIET_COKE\",\"TEA\"]}", Helpers.contentAsString(result));
    }

    @Test
    public void test_buy() {
        {
            Http.RequestBuilder request = new Http.RequestBuilder()
                    .method(POST)
                    .uri("/vending/money/100");

            Result result = route(app, request);
            assertEquals(result.toString(), OK, result.status());
        }
        {
            Http.RequestBuilder request = new Http.RequestBuilder()
                    .method(POST)
                    .uri("/vending/drink/0");

            Result result = route(app, request);
            assertEquals(result.toString(), OK, result.status());
        }
    }

    @Test
    public void test_refund() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/vending/money");

        Result result = route(app, request);
        assertEquals(OK, result.status());
        assertEquals("{\"refund\":[]}", Helpers.contentAsString(result));
    }
}
