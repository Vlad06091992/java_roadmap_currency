package currency.app.servlets;

import currency.app.Exceptions.NotFoundException;
import currency.app.Exceptions.NotValidDataException;
import currency.app.Utilites.JSONUtils;
import currency.app.Utilites.Utils;
import currency.app.DAO.ExchangeRatesDAO;
import currency.app.entities.Currency;
import currency.app.entities.Exchange;
import currency.app.entities.ExchangeRate;
import currency.app.services.ExchangeService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    private final ExchangeService exchangeService = new ExchangeService();
    private final Utils utils = new Utils();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();

        try {
            String[] queryParams = new String[]{"from", "to", "amount"};
            Map<String, String> parsedQueryParams = utils.parseQueryParams(request, response, queryParams);
            String from = parsedQueryParams.get("from");
            String to = parsedQueryParams.get("to");
            String amount = parsedQueryParams.get("amount");

            String exchange = exchangeService.exchange(from,to,amount);
            out.print(exchange);

        } catch (NotValidDataException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONUtils.printError(out, e.getMessage());

        } catch (NotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            JSONUtils.printError(out, e.getMessage());
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("server error");
        }
    }
}