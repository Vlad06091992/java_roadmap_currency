package currency.app.servlets;

import currency.app.DAO.ExchangeRatesDAO;
import currency.app.Exceptions.IsExistException;
import currency.app.Utilites.JSONUtils;
import currency.app.Utilites.Utils;
import currency.app.entities.ExchangeRate;
import currency.app.services.ExchangeService;
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
    private final ExchangeRatesDAO exchangeRatesDAO = new ExchangeRatesDAO();
    private final Utils utils = new Utils();
    private final ExchangeService exchangeService = new ExchangeService();

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

            String newExchangeRate = exchangeService.createExchangeRate(baseCurrencyCode, targetCurrencyCode, rate);
            response.setStatus(HttpServletResponse.SC_CREATED);
            out.print(JSONUtils.getSimpleJson(newExchangeRate));
        } catch (IsExistException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONUtils.printError(out, ex.getMessage());
        } catch (RuntimeException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONUtils.printError(out, ex.getMessage());
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();
        try {
            String exchangeRates = exchangeService.getAllExchangeRates();
            out.print(exchangeRates);
        } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONUtils.printError(out, "Server error");
        }


    }
}