package currency.app.servlets;

import currency.app.Configs.ObjectToJson;
import currency.app.Configs.Utils;
import currency.app.DAO.CurrencyDAO;
import currency.app.entities.Currency;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    public ObjectToJson objectToJson;
    private final CurrencyDAO currencyDAO = new CurrencyDAO();
    private final Utils utils = new Utils();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {


        try {
            List<Currency> currencies = currencyDAO.findAll();
            String currencyJsonString = ObjectToJson.getSimpleJson(currencies);
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

            Optional<Currency> currency = currencyDAO.findByCode(code);
            if (currency.isPresent()) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                out.print("error: currency already exists");
                return;
            }

            Currency newCurrency = new Currency(name,code,sign);
            Currency createdCurrency = currencyDAO.save(newCurrency);
            String currencyJsonString = ObjectToJson.getSimpleJson(createdCurrency);
            out.print(currencyJsonString);

        } catch (IOException ex) {
            request.setAttribute("message", "There was an error: " + ex.getMessage());
        }
    }




}