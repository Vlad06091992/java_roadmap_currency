package currency.app.servlets;

import currency.app.Exceptions.IsExistException;
import currency.app.Exceptions.NotFoundException;
import currency.app.Exceptions.NotValidDataException;
import currency.app.Utilites.JSONUtils;
import currency.app.Utilites.Utils;
import currency.app.DAO.ExchangeRatesDAO;
import currency.app.entities.ExchangeRate;
import currency.app.services.CurrencyService;
import currency.app.services.ExchangeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeService exchangeService = new ExchangeService();
    private final Utils utils = new Utils();

    private String[] getPathData(HttpServletRequest request) {
        String codes = request.getPathInfo().substring(1);
        String from = codes.substring(0, 3);
        String to = codes.substring(3);

        if (to.isEmpty()) {
            throw new NotValidDataException("must have valid data");
        }

        return new String[]{from, to};
    }

    ;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();

        try {
            String[] codes = getPathData(request);
            String from = codes[0];
            String to = codes[1];

            String currencyJsonString = exchangeService.findByCodes(from, to);
            out.print(currencyJsonString);
        } catch (NotValidDataException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONUtils.printError(out, e.getMessage());
        } catch (NotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            JSONUtils.printError(out, e.getMessage());
        } catch (RuntimeException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("server error");
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();

        try {
            String[] codes = getPathData(request);
            String from = codes[0];
            String to = codes[1];
            Map<String, String> params = utils.parseFormData(request);
            String rate = params.get("rate");
            if (rate.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }


            String currencyJsonString = exchangeService.updateExchangeRate(from, to, rate);
            out.print(currencyJsonString);

        } catch (NotValidDataException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONUtils.printError(out, e.getMessage());
        } catch (NotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            JSONUtils.printError(out, e.getMessage());
        } catch (RuntimeException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONUtils.printError(out, "Server error");
        }
    }
}