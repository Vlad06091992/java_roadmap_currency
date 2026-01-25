package currency.app.servlets;

import currency.app.Exceptions.NotFoundException;
import currency.app.Utilites.JSONUtils;
import currency.app.DAO.CurrencyDAO;
import currency.app.entities.Currency;
import currency.app.services.CurrencyService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    public JSONUtils jsonUtils;
    private final CurrencyDAO currencyDAO = new CurrencyDAO();
    private final CurrencyService currencyService = new CurrencyService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();
        try {
            String code = request.getPathInfo().substring(1);
            String currencyJsonString = currencyService.getCurrencyByCode(code);
            out.print(currencyJsonString);

        }
        catch (NotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            JSONUtils.printError(out, e.getMessage());
        }
    }


}