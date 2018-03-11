package model.vending.coin;

import model.vending.drink.Drink;
import model.vending.message.Refunded;

public class Safe {
    private SeparatedSafe bank_;
    private CoinStorage paybackStorage_;
    private static final Money JP_100_YEN = Money.of(100);
    private static final Money JP_500_YEN = Money.of(500);

    public Safe() {
        bank_ = new SeparatedSafe();
        bank_.addStorage(JP_100_YEN, 10);
        bank_.addStorage(JP_500_YEN, 0);
        paybackStorage_ = new CoinStorage();
    }

    public boolean available(Money amount) {
        return bank_.hasStorage(amount) && canRefund(amount);
    }

    private boolean canRefund(Money amount) {
        return amount.equals(JP_100_YEN) || bank_.canRefund(amount, JP_100_YEN);
    }

    public boolean payoff(Drink drink) {
        Money value = drink.getValue();
        if (!paybackStorage_.find(value)){
            return false;
        }
        Money amount = paybackStorage_.takeAllMoney();
        bank_.take(value, amount).forEach(this::storeToPaybackable);
        bank_.cache(value);
        return true;
    }

    public void storeToPaybackable(Money amount) {
        paybackStorage_.store(amount);
    }

    public Money getAmount() {
        return paybackStorage_.getAllMoney();
    }

    public Refunded refund() {
        return new Refunded(paybackStorage_.takeAll());
    }
}
