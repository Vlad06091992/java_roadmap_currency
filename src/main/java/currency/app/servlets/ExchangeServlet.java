package currency.app.servlets;

import currency.app.Utilites.Utils;
import currency.app.container.AppContext;
import currency.app.services.ExchangeService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    private final Utils utils = AppContext.getInstance().getUtils();
    private final ExchangeService exchangeService = AppContext.getInstance().getExchangeService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();
            String[] queryParams = new String[]{"from", "to", "amount"};
            Map<String, String> parsedQueryParams = utils.parseQueryParams(request, response, queryParams);
            String from = parsedQueryParams.get("from");
            String to = parsedQueryParams.get("to");
            String amount = parsedQueryParams.get("amount");

            String exchange = exchangeService.exchange(from,to,amount);
            out.print(exchange);
    }
}