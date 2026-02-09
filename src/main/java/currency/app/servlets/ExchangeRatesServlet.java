package currency.app.servlets;

import currency.app.DAO.ExchangeRatesDAO;
import currency.app.Exceptions.IsExistException;
import currency.app.Exceptions.NotValidDataException;
import currency.app.Utilites.JSONUtils;
import currency.app.Utilites.Utils;
import currency.app.container.AppContext;
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
    private final Utils utils = AppContext.getInstance().getUtils();
    private final ExchangeService exchangeService = AppContext.getInstance().getExchangeService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();


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
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();
        String exchangeRates = exchangeService.getAllExchangeRates();
        out.print(JSONUtils.getSimpleJson(exchangeRates));
    }
}