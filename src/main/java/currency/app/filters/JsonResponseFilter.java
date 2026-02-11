package currency.app.filters;

import currency.app.Exceptions.IsExistException;
import currency.app.Exceptions.NotFoundException;
import currency.app.Exceptions.NotValidDataException;
import currency.app.Exceptions.ServiceException;
import currency.app.Utilites.JSONUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebFilter("/*")
public class JsonResponseFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        PrintWriter out = httpResponse.getWriter();
        try {


            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");

            httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            httpResponse.setHeader("Pragma", "no-cache");
            httpResponse.setHeader("Expires", "0");

            chain.doFilter(request, httpResponse);
        } catch (IsExistException | NotValidDataException e) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONUtils.printError(out, e.getMessage());
        } catch (NotFoundException e) {
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            JSONUtils.printError(out, e.getMessage());
        } catch (ServiceException e) {
            System.out.println("error: " +e.getMessage());
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONUtils.printError(out, e.getMessage());
        } catch (Exception e) {
            System.out.println("error: " +e.getMessage());
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONUtils.printError(out, e.getMessage());
        }
    }

}