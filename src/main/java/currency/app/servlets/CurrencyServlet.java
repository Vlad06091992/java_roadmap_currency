package currency.app.servlets;

import currency.app.Exceptions.NotFoundException;
import currency.app.Utilites.JSONUtils;
import currency.app.container.AppContext;
import currency.app.services.CurrencyService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyService currencyService = AppContext.getInstance().getCurrencyService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();

            String code = request.getPathInfo().substring(1);
            String currencyJsonString = currencyService.getCurrencyByCode(code);
            out.print(currencyJsonString);
    }


}