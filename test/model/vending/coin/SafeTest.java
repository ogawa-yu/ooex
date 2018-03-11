package model.vending.coin;

import model.vending.drink.Drink;
import model.vending.drink.DrinkKind;
import model.vending.message.Refunded;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(Theories.class)
public class SafeTest {
    private static final Money COIN_10_YEN = Money.of(10);
    private static final Money COIN_100_YEN = Money.of(100);
    private static final Money COIN_500_YEN = Money.of(500);

    @Test
    public void test_available_not_storable() {
        Safe testee = new Safe();

        assertFalse(testee.available(COIN_10_YEN));
    }

    @Test
    public void test_available_100_yen() {
        Safe testee = new Safe();

        assertTrue(testee.available(COIN_100_YEN));
    }

    @Test
    public void test_available_500_yen() {
        Safe testee = new Safe();

        assertTrue(testee.available(COIN_500_YEN));
    }

    @Test
    public void test_storeToPaybackStorage() {
        Safe testee = new Safe();
        testee.storeToPaybackable(COIN_100_YEN);

        assertThat(testee.getAmount(), is(COIN_100_YEN));
    }

    @DataPoints
    public static List<Money>[] createGetAmountParameters() {
        List<Money>[] data = new List[5];
        data[0] = amounts(Money.of(0));
        data[1] = amounts(Money.of(10));
        data[2] = amounts(Money.of(100));
        data[3] = amounts(Money.of(500));
        data[4] = amounts(Money.of(100), Money.of(50));
        return data;
    }

    private static List<Money> amounts(Money ... ms) {
        return Arrays.asList(ms);
    }

    @Theory
    public void test_getAmount(List<Money> amounts) {
        Safe testee = new Safe();
        amounts.forEach(testee::storeToPaybackable);
        Money expected = Money.of(sumOf(amounts));
        Money actual = testee.getAmount();
        assertThat(actual, is(expected));
    }

    @Theory
    public void test_refund(List<Money> amounts) {
        Safe testee = new Safe();
        amounts.forEach(testee::storeToPaybackable);

        List<Money> refunds = amounts.stream().filter(m -> m.getValue() != 0).collect(Collectors.toList());
        Collections.reverse(refunds);
        Refunded expected = new Refunded(refunds);
        Refunded actual = testee.refund();
        assertThat(actual, is(expected));
    }

    @Theory
    public void test_payoff(List<Money> amounts) {
        Safe testee = new Safe();
        amounts.forEach(testee::storeToPaybackable);

        Drink item = new Drink(DrinkKind.COKE);
        int total = sumOf(amounts);
        if (!item.canBuy(Money.of(total))) {
            assertThrowIfZeroYen(testee, item);
            return;
        }

        assertTrue(testee.payoff(item));
    }

    @Test
    public void test_refund_after_payoff_100_yen() {
        Safe testee = new Safe();
        testee.storeToPaybackable(COIN_100_YEN);

        Drink item = new Drink(DrinkKind.COKE);
        testee.payoff(item);
        assertThat(testee.refund(), is(new Refunded(Collections.emptyList())));
    }

    @Test
    public void test_refund_after_payoff_110_yen() {
        Safe testee = new Safe();
        testee.storeToPaybackable(COIN_100_YEN);
        testee.storeToPaybackable(COIN_10_YEN);

        Drink item = new Drink(DrinkKind.COKE);
        testee.payoff(item);
        assertThat(testee.refund(), is(new Refunded(Arrays.asList(COIN_10_YEN))));
    }

    @Test
    public void test_refund_after_payoff_500_yen() {
        Safe testee = new Safe();
        testee.storeToPaybackable(COIN_500_YEN);

        Drink item = new Drink(DrinkKind.COKE);
        testee.payoff(item);
        assertThat(testee.refund(), is(Refunded.of(COIN_100_YEN, COIN_100_YEN, COIN_100_YEN, COIN_100_YEN)));
    }

    private void assertThrowIfZeroYen(Safe testee, Drink item) {
        assertFalse(testee.payoff(item));
    }

    private int sumOf(List<Money> amounts) {
        return amounts.stream().mapToInt(Money::getValue).sum();
    }
}
