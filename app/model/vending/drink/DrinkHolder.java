package model.vending.drink;

import model.vending.coin.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class DrinkHolder {
    private final int MAX_COUNT = 5;
    private final List<Drink> stock_;

    public DrinkHolder() {
        stock_ = new ArrayList<>(MAX_COUNT);
    }

    public Drink take() {
        if (stockout()) {
            return Drink.empty();
        }
        return stock_.remove(stock_.size() - 1);
    }

    public void charge(Drink drink) {
        stock_.add(drink);
    }

    public void charge(Drink drink, int count) {
        IntStream.range(0, count).forEach(i -> charge(drink));
    }

    public boolean stockout() {
        return stock_.isEmpty();
    }

    public boolean canBuy(Money amount) {
        if (stockout()) {
            return false;
        }
        return stock_.get(0).canBuy(amount);
    }
}
