package model.vending.message;

import lombok.Data;
import model.vending.drink.DrinkKind;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
@Data(staticConstructor = "of")
public class Buyable implements Serializable {
    private final List<DrinkKind> kinds;
}
