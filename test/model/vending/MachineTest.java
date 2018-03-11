package model.vending;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.testkit.javadsl.TestKit;
import akka.util.Timeout;
import model.vending.drink.DrinkKind;
import model.vending.message.Refunded;
import model.vending.message.AllDrinks;
import model.vending.message.Buy;
import model.vending.message.Buyable;
import model.vending.drink.Drink;
import model.vending.coin.Money;
import model.vending.message.Pay;
import model.vending.message.Refund;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import scala.reflect.ClassTag;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 下記の仕様を確認する <br>
 * - 購入できるのは コーラ・ダイエットコーラ・紅茶のみ。 <br>
 * - 購入に利用できる貨幣は500円・100円のみ。 <br>
 * - 購入に使用しなかった貨幣は払い戻すことができる。 <br>
 * - 釣り銭が不足している場合、購入することができない。
 */
public class MachineTest extends TestKit{
    private static ActorSystem system_ = ActorSystem.create("TestSystem");

    public MachineTest() {
        super(system_);
    }

    private Buyable fullyKinds() {
        return Buyable.of(Arrays.stream(DrinkKind.values())
                .filter(e -> e != DrinkKind.UNDEF)
                .sorted(Comparator.comparing(DrinkKind::getType))
                .collect(Collectors.toList()));
    }

    private Buyable buyableOther(DrinkKind kind) {
        return Buyable.of(Arrays.stream(DrinkKind.values())
                .filter(e -> e != DrinkKind.UNDEF && e != kind)
                .sorted(Comparator.comparing(DrinkKind::getType))
                .collect(Collectors.toList()));
    }

    private Buyable unbuyable() {
        return Buyable.of(Collections.emptyList());
    }

    @AfterClass
    public static void teardownClass() {
        system_.terminate();
    }

    @Test
    public void test_all_drinks() {
        ActorRef testee = getSystem().actorOf(Props.create(Machine.class), "test_all_drinks");

        testee.tell(new AllDrinks(), getRef());

        expectMsg(DrinkKind.valueList());
    }

    @Test
    public void test_put_and_refund() {
        ActorRef testee = getSystem().actorOf(Props.create(Machine.class), "test_put_and_refund");

        testee.tell(Pay.of(Money.of(50)), getRef());
        expectMsg(unbuyable());

        testee.tell(Pay.of(Money.of(100)), getRef());
        expectMsg(fullyKinds());

        testee.tell(new Refund(), getRef());
        expectMsg(Refunded.of(Money.of(100), Money.of(50)));
    }

    @Test
    public void test_all_drinks_case_await() throws Exception {
        ActorRef testee = getSystem().actorOf(Props.create(Machine.class), "test_all_drinks_case_await");

        FiniteDuration duration = Duration.create(5, TimeUnit.SECONDS);
        List<Integer> actual = Await.result(
                Patterns.ask(testee, new AllDrinks(), new Timeout(duration))
                        .mapTo(new ClassTag<List>() {
                            @Override
                            public Class<?> runtimeClass() {
                                return List.class;
                            }}),
                duration);
        List<Integer> expected = DrinkKind.valueList();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void test_buy_case_zero_yen() {
        ActorRef testee = getSystem().actorOf(Props.create(Machine.class), "test_buy_case_zero_count");

        testee.tell(Pay.of(Money.of(0)), getRef());
        expectMsg(unbuyable());

        testee.tell(Buy.of(DrinkKind.COKE), getRef());
        expectMsg(Drink.empty());
    }

    @Test
    public void test_buy_case_less_money() {
        ActorRef testee = getSystem().actorOf(Props.create(Machine.class), "test_buy_case_less_money");

        testee.tell(Pay.of(Money.of(99)), getRef());
        expectMsg(unbuyable());

        testee.tell(Buy.of(DrinkKind.COKE), getRef());
        expectMsg(Drink.empty());
    }

    @Test
    public void test_buy_case_invalid_amount() {
        ActorRef testee = getSystem().actorOf(Props.create(Machine.class), "test_buy_case_invalid_amount");

        testee.tell(Pay.of(Money.of(150)), getRef());
        expectMsg(unbuyable());

        testee.tell(Buy.of(DrinkKind.COKE), getRef());
        expectMsg(Drink.empty());
    }

    @Test
    public void test_buy_case_invalid_drink_kind() {
        ActorRef testee = getSystem().actorOf(Props.create(Machine.class), "test_buy_case_invalid_drink_kind");

        testee.tell(Pay.of(Money.of(100)), getRef());
        expectMsg(fullyKinds());

        testee.tell(Buy.of(DrinkKind.UNDEF), getRef());
        expectMsg(Drink.empty());

        testee.tell(new Refund(), getRef());
        expectMsg(Refunded.of(Money.of(100)));
    }

    @Test
    public void test_buy_case_coke() {
        ActorRef testee = getSystem().actorOf(Props.create(Machine.class), "test_buy_case_coke");

        testee.tell(Pay.of(Money.of(100)), getRef());
        expectMsg(fullyKinds());

        testee.tell(Buy.of(DrinkKind.COKE), getRef());
        expectMsg(new Drink(DrinkKind.COKE));
    }

    @Test
    public void test_buy_case_tea() {
        ActorRef testee = getSystem().actorOf(Props.create(Machine.class), "test_buy_case_tea");

        testee.tell(Pay.of(Money.of(100)), getRef());
        expectMsg(fullyKinds());

        testee.tell(Buy.of(DrinkKind.TEA), getRef());
        expectMsg(new Drink(DrinkKind.TEA));
    }

    @Test
    public void test_buy_case_diet_coke() {
        ActorRef testee = getSystem().actorOf(Props.create(Machine.class), "test_buy_case_diet_coke");

        testee.tell(Pay.of(Money.of(100)), getRef());
        expectMsg(fullyKinds());

        testee.tell(Buy.of(DrinkKind.DIET_COKE), getRef());
        expectMsg(new Drink(DrinkKind.DIET_COKE));
    }

    @Test
    public void test_refund_case_put_zero_yen() {
        ActorRef testee = getSystem().actorOf(Props.create(Machine.class), "test_refund_case_put_zero_yen");

        testee.tell(new Refund(), getRef());
        expectMsg(Refunded.of());
    }

    @Test
    public void test_refund_case_after_buy_juice_less_amount() {
        ActorRef testee = getSystem().actorOf(Props.create(Machine.class), "test_refund_case_after_buy_juice_less_amount");

        testee.tell(Pay.of(Money.of(99)), getRef());
        expectMsg(unbuyable());

        testee.tell(Buy.of(DrinkKind.COKE), getRef());
        expectMsg(Drink.empty());

        testee.tell(new Refund(), getRef());
        expectMsg(Refunded.of(Money.of(99)));
    }

    @Test
    public void test_refund_case_after_buy_juice_just_amount() {
        ActorRef testee = getSystem().actorOf(Props.create(Machine.class), "test_refund_case_after_buy_juice_just_amount");

        testee.tell(Pay.of(Money.of(100)), getRef());
        expectMsg(fullyKinds());

        testee.tell(Buy.of(DrinkKind.DIET_COKE), getRef());
        expectMsg(new Drink(DrinkKind.DIET_COKE));

        testee.tell(new Refund(), getRef());
        expectMsg(Refunded.of());
    }

    @Test
    public void test_refund_case_after_buy_juice_more_amount() {
        ActorRef testee = getSystem().actorOf(Props.create(Machine.class), "test_refund_case_after_buy_juice_more_amount");

        testee.tell(Pay.of(Money.of(500)), getRef());
        expectMsg(fullyKinds());

        testee.tell(Buy.of(DrinkKind.TEA), getRef());
        expectMsg(new Drink(DrinkKind.TEA));

        testee.tell(new Refund(), getRef());
        expectMsg(Refunded.of(Money.of(100), Money.of(100), Money.of(100), Money.of(100)));
    }

    @Test
    public void test_refund_case_has_exceed_amount() {
        ActorRef testee = getSystem().actorOf(Props.create(Machine.class), "test_refund_case_invalid_amount");

        testee.tell(Pay.of(Money.of(50)), getRef());
        expectMsg(Buyable.of(Collections.emptyList()));

        testee.tell(Pay.of(Money.of(100)), getRef());
        expectMsg(fullyKinds());

        testee.tell(Buy.of(DrinkKind.COKE), getRef());
        expectMsg(new Drink(DrinkKind.COKE));

        testee.tell(new Refund(), getRef());
        expectMsg(Refunded.of(Money.of(50)));
    }

    @Test
    public void test_buy_case_poor_change() {
        ActorRef testee = getSystem().actorOf(Props.create(Machine.class), "test_buy_case_poor_change");
        Refunded payback = Refunded.of(Money.of(100), Money.of(100), Money.of(100), Money.of(100));

        testee.tell(Pay.of(Money.of(500)), getRef());
        expectMsg(fullyKinds());

        testee.tell(Buy.of(DrinkKind.COKE), getRef());
        expectMsg(new Drink(DrinkKind.COKE));
        testee.tell(new Refund(), getRef());
        expectMsg(payback);

        testee.tell(Pay.of(Money.of(500)), getRef());
        expectMsg(fullyKinds());

        testee.tell(Buy.of(DrinkKind.COKE), getRef());
        expectMsg(new Drink(DrinkKind.COKE));
        testee.tell(new Refund(), getRef());
        expectMsg(payback);

        testee.tell(Pay.of(Money.of(500)), getRef());
        expectMsg(unbuyable());

        testee.tell(Buy.of(DrinkKind.COKE), getRef());
        expectMsg(Drink.empty());
        testee.tell(new Refund(), getRef());
        expectMsg(Refunded.of(Money.of(500)));
    }

    @Test
    public void test_buy_case_poor_stock() {
        ActorRef testee = getSystem().actorOf(Props.create(Machine.class), "test_buy_case_poor_stock");

        test_poor_stock(testee, DrinkKind.COKE);
        test_poor_stock(testee, DrinkKind.DIET_COKE);
        test_poor_stock(testee, DrinkKind.TEA);
    }

    private void test_poor_stock(ActorRef testee, DrinkKind kind) {
        IntStream.range(0, 5).forEach(i ->{
            testee.tell(Pay.of(Money.of(100)), getRef());
            expectMsg(fullyKinds());

            testee.tell(Buy.of(kind), getRef());
            expectMsg(new Drink(kind));
        });

        testee.tell(Pay.of(Money.of(100)), getRef());
        expectMsg(buyableOther(kind));

        testee.tell(Buy.of(kind), getRef());
        expectMsg(Drink.empty());

        testee.tell(new Refund(), getRef());
        expectMsg(Refunded.of(Money.of(100)));
    }
}
