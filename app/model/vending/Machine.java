package model.vending;

import akka.actor.AbstractActor;
import model.vending.coin.Safe;
import model.vending.drink.DrinkHolder;
import model.vending.drink.DrinkKind;
import model.vending.drink.DrinkServer;
import model.vending.message.AllDrinks;
import model.vending.message.Buy;
import model.vending.message.Buyable;
import model.vending.drink.Drink;
import model.vending.coin.Money;
import model.vending.message.Pay;
import model.vending.message.Refund;

import java.util.Collections;

public class Machine extends AbstractActor {
    private final DrinkServer storage_ = new DrinkServer();
    private final Safe safe_ = new Safe();

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(AllDrinks.class, msg ->
            sender().tell(DrinkKind.valueList(), self())
        ).match(Pay.class,    msg -> sender().tell(handle(msg.getAmount()), self())
        ).match(Buy.class,    msg -> sender().tell(handle(msg.getDrinkType()), self())
        ).match(Refund.class, msg -> sender().tell(safe_.refund(), self())
        ).build();
    }

    private Buyable handle(Money amount) {
        safe_.storeToPaybackable(amount);
        if (!safe_.available(amount)) {
            return Buyable.of(Collections.emptyList());
        }
        return storage_.buyable(safe_.getAmount());
    }

    private Drink handle(DrinkKind kindOfDrink) {
        DrinkHolder holder = storage_.select(kindOfDrink);
        Drink drink = holder.take();
        if (!safe_.payoff(drink)) {
            return Drink.empty();
        }
        return drink;
    }
}
