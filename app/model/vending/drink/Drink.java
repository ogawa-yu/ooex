package model.vending.drink;

import lombok.Data;
import model.vending.coin.Money;

import java.io.Serializable;

@SuppressWarnings("serial")
public @Data class Drink implements Serializable {
    private final DrinkKind kind;
    private final Money value;

    public Drink(DrinkKind kind) {
        this(kind, Money.of(100));
    }

    public Drink(DrinkKind kind, Money value) {
        this.kind = kind;
        this.value = value;
    }

    public boolean canBuy(Money amount) {
        if (this.kind == DrinkKind.UNDEF) {
            return false;
        }
        return value.getValue() <= amount.getValue();
    }

    public static Drink empty() {
        return new Drink(DrinkKind.UNDEF, Money.of(0));
    }
}
