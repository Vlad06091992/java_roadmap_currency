package currency.app.entities;

import java.math.BigDecimal;

public class Exchange extends ExchangeRate {

    private BigDecimal amount;
    private BigDecimal convertedAmount;

    public Exchange(Currency baseCurrency, Currency targetCurrency, BigDecimal rate,BigDecimal amount) {
        super(baseCurrency, targetCurrency, rate);
        this.amount = amount;
        this.convertedAmount = amount.multiply(rate);
    }
}
