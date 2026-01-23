package currency.app.servlets;

import currency.app.DAO.ExchangeRatesDAO;
import currency.app.Utilites.JSONUtils;
import currency.app.Utilites.Utils;
import currency.app.entities.ExchangeRate;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet("/exchangeRates/*")
public class ExchangeRatesServlet extends HttpServlet {
    public JSONUtils jsonUtils;
    private final ExchangeRatesDAO exchangeRatesDAO = new ExchangeRatesDAO();
    private final Utils utils = new Utils();

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


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();
        try {
            List<ExchangeRate> exchangeRates = exchangeRatesDAO.findAll();
            out.print(JSONUtils.getSimpleJson(exchangeRates));
        } catch (SQLException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }


    }
}