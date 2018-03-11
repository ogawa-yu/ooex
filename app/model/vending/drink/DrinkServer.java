package model.vending.drink;

import model.vending.message.Buyable;
import model.vending.coin.Money;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DrinkServer {
    private final Map<DrinkKind, DrinkHolder> server_;
    private static final DrinkHolder EMPTY_HOLDER = new DrinkHolder();

    public DrinkServer() {
        server_ = new HashMap<>();
        Arrays.stream(DrinkKind.values())
                .filter(kind -> kind != DrinkKind.UNDEF)
                .forEach(kind -> {
                    DrinkHolder holder = new DrinkHolder();
                    holder.charge(new Drink(kind), 5);
                    server_.put(kind, holder);
                });
    }

    public boolean stockout(DrinkKind kind) {
        if (!server_.containsKey(kind)) {
            return true;
        }
        DrinkHolder holder = server_.get(kind);
        return holder.stockout();
    }

    public DrinkHolder select(DrinkKind kind) {
        return server_.getOrDefault(kind, EMPTY_HOLDER);
    }

    public Buyable buyable(Money amount) {
        List<DrinkKind> buyableDrinks = server_.entrySet()
                .stream()
                .filter(entry -> entry.getValue().canBuy(amount))
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparing(DrinkKind::getType))
                .collect(Collectors.toList());
        return Buyable.of(buyableDrinks);
    }
}
