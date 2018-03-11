package model.vending.coin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SeparatedSafe {
    private Map<Money, CoinStorage> storage_;

    SeparatedSafe() {
        storage_ = new HashMap<>();
    }

    public void addStorage(Money money, int count) {
        if (!money.isValid()) {
            return;
        }
        storage_.put(money, new CoinStorage(count, money));
    }

    public boolean hasStorage(Money money) {
        return storage_.containsKey(money);
    }

    public boolean canRefund(Money amount, Money atomicValue) {
        CoinStorage refundCoins =  storage_.get(atomicValue);
        int paybackCount = amount.numberOfDifference(atomicValue);
        return refundCoins.hasCoins(paybackCount);
    }

    public void cache(Money amount) {
        if (!storage_.containsKey(amount)) {
            throw new IllegalArgumentException("amount cannot available. amount: " + amount);
        }
        CoinStorage coins = storage_.get(amount);
        coins.store(amount);
    }

    public List<Money> take(Money target, Money amount) {
        CoinStorage coins = storage_.get(target);
        int countOfPayoff = amount.numberOfDifference(target);
        Money rest = amount.difference(target);
        List<Money> take = coins.take(countOfPayoff);
        take.add(rest);
        return take;
    }
}
