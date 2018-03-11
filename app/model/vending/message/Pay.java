package model.vending.message;

import lombok.Data;
import model.vending.coin.Money;

import java.io.Serializable;

@SuppressWarnings("serial")
@Data(staticConstructor = "of")
public class Pay implements Serializable {
    private final Money amount;
}
