package currency.app.servlets;

import currency.app.Configs.ObjectToJson;
import currency.app.Configs.Utils;
import currency.app.DAO.ExchangeRatesDAO;
import currency.app.entities.ExchangeRate;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.Map;
import java.util.Optional;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    public ObjectToJson objectToJson;
    private final ExchangeRatesDAO exchangeRatesDAO = new ExchangeRatesDAO();
    private final Utils utils = new Utils();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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

            Optional<ExchangeRate> exchangeRate = exchangeRatesDAO.findByCodes(from, to);
            if (exchangeRate.isPresent()) {
                String currencyJsonString = ObjectToJson.getSimpleJson(exchangeRate.get());
                out.print(currencyJsonString);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("not exist");
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
                    String currencyJsonString = ObjectToJson.getSimpleJson(newExchangeRate);
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
                    String currencyJsonString = ObjectToJson.getSimpleJson(exchangeRate.get());
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