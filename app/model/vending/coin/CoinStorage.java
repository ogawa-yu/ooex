package model.vending.coin;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class CoinStorage {
    private List<Money> storage_;

    CoinStorage() {
        this(0, Money.of(0));
    }

    CoinStorage(int count, Money money) {
        storage_ = IntStream.range(0, count)
                .mapToObj(i -> Money.of(money.getValue()))
                .collect(Collectors.toList());
    }

    public boolean hasCoins(int count) {
        return count <= storage_.size();
    }

    public Money take() {
        if (storage_.isEmpty()) {
            return Money.of(0);
        }
        return storage_.remove(storage_.size() - 1);
    }

    public List<Money> take(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> take())
                .collect(Collectors.toList());
    }

    public List<Money> takeAll() {
        return IntStream.range(0, storage_.size())
                .mapToObj(i -> take())
                .collect(Collectors.toList());
    }

    public boolean find(Money target) {
        return storage_.stream()
                .filter(m -> m.toCurrency() != null)
                .anyMatch(m -> (target.getValue() <= m.getValue()));
    }

    public Money getAllMoney() {
        return sum(storage_.stream());
    }

    public Money takeAllMoney() {
        return sum(takeAll().stream());
    }

    private Money sum(Stream<Money> moneyStream) {
        return Money.of(moneyStream.mapToInt(Money::getValue).sum());
    }

    public void store(Money coin) {
        if (coin.isValid()) {
            storage_.add(coin);
        }
    }
}
