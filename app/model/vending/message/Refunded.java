package model.vending.message;

import lombok.Data;
import model.vending.coin.Money;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Data
public class Refunded implements Serializable {
    private final List<Money> refund;

    public static Refunded of(Money ... paybacks) {
        return new Refunded(Arrays.asList(paybacks));
    }

    public Refunded(List<Money> paybacks) {
        refund = paybacks;
    }
}
