package currency.app.servlets;

import currency.app.Utilites.JSONUtils;
import currency.app.DAO.CurrencyDAO;
import currency.app.entities.Currency;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    public JSONUtils JSONUtils;
    private final CurrencyDAO currencyDAO = new CurrencyDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {


        try {
            String code = request.getPathInfo().substring(1);
            Optional<Currency> currency = currencyDAO.findByCode(code);

            if (currency.isEmpty()) {
                response.sendError(HttpServletResponse.SC_PAYMENT_REQUIRED);
                return;
            }

            System.out.println(currency);
            String currencyJsonString = JSONUtils.getSimpleJson(currency.get());
            PrintWriter out = response.getWriter();
            out.print(currencyJsonString);
            out.flush();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }


}