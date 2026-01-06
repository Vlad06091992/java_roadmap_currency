package currency.app.servlets;

import currency.app.Utilites.JSONUtils;
import currency.app.Utilites.Utils;
import currency.app.DAO.ExchangeRatesDAO;
import currency.app.entities.Currency;
import currency.app.entities.Exchange;
import currency.app.entities.ExchangeRate;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    public JSONUtils JSONUtils;
    private final ExchangeRatesDAO exchangeRatesDAO = new ExchangeRatesDAO();
    private final Utils utils = new Utils();
    private final BigDecimal hundred = BigDecimal.valueOf(100);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();

        try {

            String[] queryParams = new String[]{"from", "to", "amount"};
            Map<String, String> parsedQueryParams = utils.parseQueryParams(request, response, queryParams);
            String from = parsedQueryParams.get("from");
            String to = parsedQueryParams.get("to");
            String amount = parsedQueryParams.get("amount");
            BigDecimal amountValue = BigDecimal.valueOf(Long.parseLong(amount));
            Optional<ExchangeRate> exchangeRate = Optional.empty();

            Optional<ExchangeRate> excRate = exchangeRatesDAO.findByCodes(from, to);
            if (excRate.isPresent()) {
                exchangeRate = excRate;
            } else {
                Optional<ExchangeRate> reversedExchangeRate = exchangeRatesDAO.findByCodes(to, from);
                if (reversedExchangeRate.isPresent()) {
                    Currency targetCurrency = reversedExchangeRate.get().getTargetCurrency();
                    Currency baseCurrency = reversedExchangeRate.get().getBaseCurrency();
                    BigDecimal rate = hundred.divide(reversedExchangeRate.get().getRate().multiply(hundred), 7, RoundingMode.HALF_UP);
                    reversedExchangeRate.get().setBaseCurrency(targetCurrency);
                    reversedExchangeRate.get().setTargetCurrency(baseCurrency);
                    reversedExchangeRate.get().setRate(rate);
                    exchangeRate = reversedExchangeRate;

                }
            }

            if (exchangeRate.isPresent()) {
                Exchange exchange = new Exchange(exchangeRate.get().getBaseCurrency(), exchangeRate.get().getTargetCurrency(), exchangeRate.get().getRate(), amountValue);
                String currencyJsonString = JSONUtils.getSimpleJsonWithoutId(exchange);
                out.print(currencyJsonString);
            } else {
                Optional<ExchangeRate> usdFrom = exchangeRatesDAO.findByCodes("USD", from);
                Optional<ExchangeRate> usdTo = exchangeRatesDAO.findByCodes("USD", to);


                if (usdFrom.isEmpty() || usdTo.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("not exist");
                    return;
                }

                BigDecimal usdFromRate = usdFrom.get().getRate();
                BigDecimal usdToRate = usdTo.get().getRate();

                BigDecimal resultRate = usdToRate.divide(usdFromRate, 7, RoundingMode.HALF_UP);
                Exchange exchange = new Exchange(usdFrom.get().getTargetCurrency(), usdTo.get().getTargetCurrency(), resultRate, amountValue);
                String currencyJsonString = JSONUtils.getSimpleJsonWithoutId(exchange);
                out.print(currencyJsonString);
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("not exist");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();

        try {
            Map<String, String> params = utils.parseFormData(request);
            String baseCurrencyCode = params.get("baseCurrencyCode");
            String targetCurrencyCode = params.get("targetCurrencyCode");
            String rate = params.get("rate");
            for (String param : new String[]{baseCurrencyCode, targetCurrencyCode, rate}) {
                if (params.isEmpty() || param == null) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }


            response.setStatus(HttpServletResponse.SC_CREATED);

            Optional<ExchangeRate> exchangeRate = exchangeRatesDAO.findByCodes(baseCurrencyCode, targetCurrencyCode);
            if (exchangeRate.isPresent()) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                out.print("already exist");

            } else {
                BigDecimal rateValue = BigDecimal.valueOf(Double.parseDouble(rate));
                Optional<ExchangeRate> newExchangeRate = exchangeRatesDAO.create(baseCurrencyCode, targetCurrencyCode, rateValue);

                if (newExchangeRate.isPresent()) {
                    String currencyJsonString = JSONUtils.getSimpleJson(newExchangeRate);
                    out.print(currencyJsonString);
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }

        } catch (Exception ex) {
            if (ex instanceof NumberFormatException) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
            }
        }
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();

        try {
            String codes = request.getPathInfo().substring(1);
            String from = codes.substring(0, 3);
            String to = codes.substring(3);

            if (from.isEmpty() || to.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }


            Map<String, String> params = utils.parseFormData(request);
            String rate = params.get("rate");
            if (rate.isEmpty() || rate == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }


            response.setStatus(HttpServletResponse.SC_CREATED);

            Optional<ExchangeRate> exchangeRate = exchangeRatesDAO.findByCodes(from, to);
            if (exchangeRate.isPresent()) {
                //делаем update
                exchangeRate.get().setRate(BigDecimal.valueOf(Double.parseDouble(rate)));
                Optional<ExchangeRate> res = exchangeRatesDAO.update(exchangeRate.get());
                if (res.isPresent()) {
                    String currencyJsonString = JSONUtils.getSimpleJson(exchangeRate.get());
                    out.print(currencyJsonString);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (Exception ex) {
            if (ex instanceof NumberFormatException) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
            }
        }
    }
}