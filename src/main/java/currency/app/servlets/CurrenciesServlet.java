package currency.app.servlets;

import currency.app.Utilites.JSONUtils;
import currency.app.Utilites.Utils;
import currency.app.DAO.CurrencyDAO;
import currency.app.entities.Currency;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    public JSONUtils jsonUtils;
    private final CurrencyDAO currencyDAO = new CurrencyDAO();
    private final Utils utils = new Utils();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {


        try {
            List<Currency> currencies = currencyDAO.findAll();
            String currencyJsonString = JSONUtils.getSimpleJson(currencies);
            PrintWriter out = response.getWriter();
            out.print(currencyJsonString);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();

        try {
            Map<String, String> params = utils.parseFormData(request);

            String name = params.get("name");
            String code = params.get("code");
            String sign = params.get("sign");

            for (String field : new String[]{code, sign, name}) {
                if(field == null || field.isEmpty()) {
                    out.print("need required fields: name,code,sign");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }

            Optional<Currency> currency = currencyDAO.findByCode(code);
            if (currency.isPresent()) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                out.print("error: currency already exists");
                return;
            }

            Currency newCurrency = new Currency(name,code,sign);
            Currency createdCurrency = currencyDAO.save(newCurrency);
            String currencyJsonString = JSONUtils.getSimpleJson(createdCurrency);
            out.print(currencyJsonString);

        } catch (IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }




}