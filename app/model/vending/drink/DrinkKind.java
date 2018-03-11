package model.vending.drink;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum DrinkKind {
    UNDEF(-1),
    COKE(0),
    DIET_COKE(1),
    TEA(2);

    private int type_;

    DrinkKind(int type) {
        type_ = type;
    }

    public int getType() {
        return type_;
    }

    public static DrinkKind fromType(int type) {
        return Arrays.stream(values())
                .filter(kind -> kind.getType() == type)
                .findFirst()
                .orElse(DrinkKind.UNDEF);
    }

    public static List<Integer> valueList() {
        return Arrays.stream(values())
                .filter(e -> e != UNDEF)
                .map(DrinkKind::getType)
                .collect(Collectors.toList());
    }
}
