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
import java.util.Map;
import java.util.Optional;

@WebServlet("/exchangeRate")
public class ExchangeRateServlet extends HttpServlet {
    public ObjectToJson objectToJson;
    //    private final CurrencyDAO currencyDAO = new CurrencyDAO();
    private final ExchangeRatesDAO exchangeRatesDAO = new ExchangeRatesDAO();
    private final Utils utils = new Utils();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();

        try {
            Map<String, String> params = utils.parseFormData(request);

            String name = params.get("name");
            String code = params.get("code");
            String sign = params.get("sign");

            Optional<ExchangeRate> exchangeRate = exchangeRatesDAO.findById(20);
            if (exchangeRate.isPresent()) {
                String currencyJsonString = ObjectToJson.getSimpleJson(exchangeRate);
                out.print(currencyJsonString);
            } else {
                out.print("not exist");
            }
        } finally {

        }
    }
}