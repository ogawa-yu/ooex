package controllers;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import model.vending.coin.Money;
import model.vending.drink.DrinkKind;
import model.vending.message.AllDrinks;
import model.vending.message.Buy;
import model.vending.message.Pay;
import model.vending.message.Refund;
import modules.ActorModule;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import scala.compat.java8.FutureConverters;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.CompletionStage;

public class VendingMachineController extends Controller {
    private ActorRef vendingMachine_;
    private static final long TIMEOUT = 1000;
    @Inject
    public VendingMachineController(@Named(ActorModule.VENDING_MACHINE_ACTOR) ActorRef actor) {
        vendingMachine_ = actor;
    }

    public CompletionStage<Result> allDrinks() {
        return handleMessage(new AllDrinks());
    }

    public CompletionStage<Result> pay(int amount) {
        return handleMessage(Pay.of(Money.of(amount)));
    }

    public CompletionStage<Result> buy(int kind) {
        return handleMessage(Buy.of(DrinkKind.fromType(kind)));
    }

    public CompletionStage<Result> refund() {
        return handleMessage(new Refund());
    }

    private <T> CompletionStage<Result> handleMessage(T message) {
        return FutureConverters.toJava(
                Patterns.ask(vendingMachine_, message, TIMEOUT))
                .thenApply(response -> ok(Json.toJson(response)));
    }
}
