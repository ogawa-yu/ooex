package model.vending.coin;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SeparatedSafeTest {
    @Test
    public void test_unhasStorage() {
        SeparatedSafe testee = new SeparatedSafe();
        assertFalse(testee.hasStorage(Money.of(0)));

        testee.addStorage(Money.of(100), 0);
        assertTrue(testee.hasStorage(Money.of(100)));
    }

    @Test
    public void test_hasStorage() {
        SeparatedSafe testee = new SeparatedSafe();
        testee.addStorage(Money.of(100), 1);

        assertTrue(testee.hasStorage(Money.of(100)));
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_cache_notFoundStorage() {
        SeparatedSafe testee = new SeparatedSafe();
        testee.addStorage(Money.of(100), 1);

        testee.cache(Money.of(200));
    }

    @Test
    public void test_cache() {
        SeparatedSafe testee = new SeparatedSafe();
        testee.addStorage(Money.of(100), 1);

        testee.cache(Money.of(100));
        List<Money> actual = testee.take(Money.of(100), Money.of(200));
        assertThat(actual.get(0), is(Money.of(100)));
        assertTrue(testee.hasStorage(Money.of(100)));
    }

    @Test
    public void test_canRefund() {
        SeparatedSafe testee = new SeparatedSafe();
        Money targetCoin = Money.of(100);
        testee.addStorage(targetCoin, 5);

        assertTrue(testee.canRefund(Money.of(100), targetCoin));
        assertTrue(testee.canRefund(Money.of(200), targetCoin));
        assertTrue(testee.canRefund(Money.of(300), targetCoin));
        assertTrue(testee.canRefund(Money.of(400), targetCoin));
        assertTrue(testee.canRefund(Money.of(500), targetCoin));
        assertTrue(testee.canRefund(Money.of(600), targetCoin));
        assertFalse(testee.canRefund(Money.of(700), targetCoin));
    }

    @Test
    public void test_take() {
        SeparatedSafe testee = new SeparatedSafe();
        Money targetCoin = Money.of(100);
        testee.addStorage(targetCoin, 5);

        {
            List<Money> actual = testee.take(targetCoin, Money.of(200));
            actual.forEach(a -> assertThat(a, is(Money.of(100))));
        }
        {
            List<Money> actual = testee.take(targetCoin, Money.of(200));
            actual.forEach(a -> assertThat(a, is(Money.of(100))));
        }
        {
            List<Money> actual = testee.take(targetCoin, Money.of(400));
            actual.forEach(a -> assertThat(a, is(Money.of(100))));
        }
        assertTrue(testee.canRefund(targetCoin, targetCoin));
        assertFalse(testee.canRefund(Money.of(200), targetCoin));
    }
}
