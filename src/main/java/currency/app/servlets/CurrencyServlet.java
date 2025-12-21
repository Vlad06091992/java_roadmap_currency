package currency.app.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import currency.app.Configs.DBSetup;
import currency.app.Configs.ObjectToJson;
import currency.app.DAO.CurrencyDAO;
import currency.app.DTO.UserDTO;
import currency.app.entities.Currency;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    public Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public ObjectToJson objectToJson;
    private CurrencyDAO currencyDAO = new CurrencyDAO(new DBSetup());

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {


        try {
            String pathInfo = request.getPathInfo().substring(1);

            int id = Integer.parseInt(pathInfo);

            Optional<Currency> currency = currencyDAO.findById(id);

            if (currency.isEmpty()) {
                response.sendError(HttpServletResponse.SC_PAYMENT_REQUIRED);
                return;
            }

            System.out.println(currency);
            String currencyJsonString = ObjectToJson.getSimpleJson(currency.get());
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(currencyJsonString);
            out.flush();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {


        try (BufferedReader reader = request.getReader()) {
            Gson gson = new Gson();
            UserDTO user = gson.fromJson(reader, UserDTO.class);

            System.out.println("NAME " + user.getName());
            System.out.println("SALARY " + user.getSalary());
            System.out.println("LAST ADDRESS " + user.getAddresses().get(2).toString());

            response.getWriter()
                    .append("Added new Product with name: ")
                    .append("some string");

        } catch (IOException ex) {
            request.setAttribute("message", "There was an error: " + ex.getMessage());
        }
    }
}