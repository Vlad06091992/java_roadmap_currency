package currency.app.entities;

import currency.app.entities.annotations.*;

import java.math.BigDecimal;

@UpdateQuery(query = "UPDATE public.exchange_rates\n" +
        "\tSET  rate=?\n" +
        "\tWHERE id = ?")
@UpdateColumns(columns = {"id", "base_currency_id", "target_currency_id", "rate"})
@InsertWithReturningIdQuery(
        query = "INSERT INTO public.exchange_rates(\n" +
                "\t base_currency_id, target_currency_id, rate)\n" +
                "\tVALUES (?, ? ,? ) RETURNING id;"
)
@Columns(columns = {
        "id",
        "target_currency_id",
        "base_currency_id",
        "rate",
        "base_currency_code",
        "base_currency_fullname",
        "base_currency_sign",
        "target_currency_code",
        "target_currency_fullname",
        "target_currency_sign"
})
@FindAllQuery(query = "SELECT \n" +
        "    exr.id as id,\n" +
        "\texr.target_currency_id,\n" +
        "\texr.base_currency_id,\n" +
        "\texr.rate as rate,\n" +
        "\tbase_currency.code as base_currency_code,\n" +
        "\tbase_currency.fullname as base_currency_fullname,\n" +
        "\tbase_currency.sign as base_currency_sign,\n" +
        "\ttarget_currency.code as target_currency_code,\n" +
        "\ttarget_currency.fullname as target_currency_fullname,\n" +
        "\ttarget_currency.sign as target_currency_sign\n" +
        "FROM public.exchange_rates as exr\n" +
        "LEFT JOIN \"currencies\" as base_currency ON exr.base_currency_id = base_currency.id\n" +
        "LEFT JOIN \"currencies\" as target_currency ON exr.target_currency_id = target_currency.id")
@FindByIdQuery(query = "SELECT \\n\" +\n" +
        "        \"    exr.id as id,\\n\" +\n" +
        "        \"\\texr.target_currency_id,\\n\" +\n" +
        "        \"\\texr.base_currency_id,\\n\" +\n" +
        "        \"\\texr.rate as rate,\\n\" +\n" +
        "        \"\\tbase_currency.code as base_currency_code,\\n\" +\n" +
        "        \"\\tbase_currency.fullname as base_currency_fullname,\\n\" +\n" +
        "        \"\\tbase_currency.sign as base_currency_sign,\\n\" +\n" +
        "        \"\\ttarget_currency.code as target_currency_code,\\n\" +\n" +
        "        \"\\ttarget_currency.fullname as target_currency_fullname,\\n\" +\n" +
        "        \"\\ttarget_currency.sign as target_currency_sign\\n\" +\n" +
        "        \"FROM public.exchange_rates as exr\\n\" +\n" +
        "        \"LEFT JOIN \\\"currencies\\\" as base_currency ON exr.base_currency_id = base_currency.id\\n\" +\n" +
        "        \"LEFT JOIN \\\"currencies\\\" as target_currency ON exr.target_currency_id = target_currency.id\")" +
        "WHERE id = ?")
public class ExchangeRate {
    private int id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;

    public ExchangeRate(Currency baseCurrency, Currency targetCurrency, BigDecimal rate, int id) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public ExchangeRate(Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public ExchangeRate(
            int id,
            int target_currency_id,
            int base_currency_id,
            BigDecimal rate,
            String base_currency_code,
            String base_currency_fullname,
            String base_currency_sign,
            String target_currency_code,
            String target_currency_fullname,
            String target_currency_sign

    ) {
        this.id = id;
        this.baseCurrency = new Currency(base_currency_fullname, base_currency_sign, base_currency_code, base_currency_id);
        this.targetCurrency = new Currency(target_currency_fullname, target_currency_sign, target_currency_code, target_currency_id);
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "id=" + id +
                ", baseCurrency=" + baseCurrency +
                ", targetCurrency=" + targetCurrency +
                ", rate=" + rate +
                '}';
    }
}
