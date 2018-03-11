package model.vending.coin;

import javassist.NotFoundException;

import java.util.stream.Stream;

public enum Currency {
    JP_10(10) {
        @Override
        public Currency lowerValue() throws NotFoundException {
            throw new NotFoundException("lower currency is not defined.");
        }
    },
    JP_50(50) {
        @Override
        public Currency lowerValue() throws NotFoundException {
            return JP_10;
        }
    },
    JP_100(100) {
        @Override
        public Currency lowerValue() throws NotFoundException {
            return JP_50;
        }
    },
    JP_500(500) {
        @Override
        public Currency lowerValue() throws NotFoundException {
            return JP_100;
        }
    };

    private int value;

    Currency(int value) {
        this.value = value;
    }

    public Money toMoney() {
        return Money.of(this.value);
    }

    public abstract Currency lowerValue() throws NotFoundException;

    public static Currency valueOf(int value) {
        return Stream.of(values())
                .filter(c -> c.value == value)
                .findFirst()
                .orElse(null);
    }
}
