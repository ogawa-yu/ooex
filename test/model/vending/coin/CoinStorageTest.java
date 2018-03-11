package model.vending.coin;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CoinStorageTest {

    @Test
    public void test_emptyStorage() {
        CoinStorage testee = new CoinStorage();
        assertTrue(testee.hasCoins(0));
        assertFalse(testee.hasCoins(1));

        Money actual = testee.take();
        assertThat(actual, is(Money.of(0)));

        assertTrue(testee.takeAll().isEmpty());
    }

    @Test
    public void test_allSameCoins() {
        CoinStorage testee = new CoinStorage(5, Money.of(100));
        assertTrue(testee.hasCoins(5));

        assertThat(testee.take(), is(Money.of(100)));
        assertThat(testee.take(), is(Money.of(100)));
        assertThat(testee.take(), is(Money.of(100)));
        assertThat(testee.take(), is(Money.of(100)));
        assertThat(testee.take(), is(Money.of(100)));
        assertThat(testee.take(), is(Money.of(0)));
        assertTrue(testee.hasCoins(0));
    }

    @Test
    public void test_zeroCoins() {
        CoinStorage testee = new CoinStorage(0, Money.of(100));
        assertTrue(testee.hasCoins(0));
        assertFalse(testee.hasCoins(1));

        Money actual = testee.take();
        assertThat(actual, is(Money.of(0)));

        assertTrue(testee.takeAll().isEmpty());
    }

    @Test
    public void test_takeAll() {
        CoinStorage testee = new CoinStorage(5, Money.of(100));
        List<Money> actual = testee.takeAll();
        assertThat(actual.size(), is(5));
    }

    @Test
    public void test_takeZero() {
        CoinStorage testee = new CoinStorage(5, Money.of(100));
        List<Money> actual = testee.take(0);
        assertTrue(actual.isEmpty());
    }

    @Test
    public  void test_store_zero_yen() {
        CoinStorage testee = new CoinStorage();

        testee.store(Money.of(0));
        assertTrue(testee.hasCoins(0));
    }

    @Test
    public  void test_store() {
        CoinStorage testee = new CoinStorage();

        testee.store(Money.of(200));
        assertTrue(testee.hasCoins(1));
    }

    @Test
    public void test_getAllMoney() {
        CoinStorage testee = new CoinStorage();

        testee.store(Money.of(100));
        testee.store(Money.of(100));
        assertThat(testee.getAllMoney(), is(Money.of(200)));
        assertTrue(testee.hasCoins(2));
    }

    @Test
    public void test_takeAllMoney() {
        CoinStorage testee = new CoinStorage();

        testee.store(Money.of(100));
        testee.store(Money.of(100));
        assertThat(testee.takeAllMoney(), is(Money.of(200)));
        assertTrue(testee.hasCoins(0));
    }

    @Test
    public void test_find() {
        CoinStorage testee = new CoinStorage();

        testee.store(Money.of(50));
        assertFalse(testee.find(Money.of(100)));

        testee.store(Money.of(100));
        assertFalse(testee.find(Money.of(150)));
        assertTrue(testee.find(Money.of(100)));
    }
}
