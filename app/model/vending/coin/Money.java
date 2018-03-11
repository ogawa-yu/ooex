package model.vending.coin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("serial")
@Data(staticConstructor = "of")
public class Money implements Serializable {
    private final int value;

    @JsonIgnore
    public boolean isValid() {
        return value > 0;
    }

    public Money difference(Money other) {
        if (this.value > other.value) {
            return Money.of(this.value - other.value);
        } else {
            return Money.of(other.value - this.value);
        }
    }

    public Currency toCurrency() {
        return Currency.valueOf(this.value);
    }

    public int numberOfDifference(Money other) {
        return (value / other.value) - 1;
    }
}
