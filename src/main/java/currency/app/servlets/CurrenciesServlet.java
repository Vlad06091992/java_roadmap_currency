package currency.app.servlets;

import currency.app.DAO.ErrorDTO;
import currency.app.Exceptions.IsExistException;
import currency.app.Exceptions.NotValidDataException;
import currency.app.Utilites.JSONUtils;
import currency.app.Utilites.Utils;
import currency.app.DAO.CurrencyDAO;
import currency.app.entities.Currency;
import currency.app.services.CurrencyService;
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
    private final Utils utils = new Utils();
    private final CurrencyService currencyService = new CurrencyService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();
        try {
            String currencyJsonString = currencyService.getAllCurrenciesStringResponse();
            out.print(currencyJsonString);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONUtils.printError(out, "Server error");
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
            String currency = currencyService.createCurrency(name, code, sign);
            out.print(currency);
        } catch (IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONUtils.printError(out, "Server error");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            JSONUtils.printError(out, e.getMessage());
        } catch (IsExistException | NotValidDataException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONUtils.printError(out, e.getMessage());

        }
    }
}