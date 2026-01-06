package currency.app.Utilites;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {
    public Map<String, String> parseFormData(HttpServletRequest request) throws IOException {

        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }

        String body = requestBody.toString();

        
        
        Map<String, String> params = new HashMap<>();
        String[] pairs = body.split("&");

        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                try {
                    String key = URLDecoder.decode(keyValue[0], "UTF-8");
                    String value = URLDecoder.decode(keyValue[1], "UTF-8");
                    params.put(key, value);
                } catch (UnsupportedEncodingException e) {
                    // Обработка ошибки
                }
            }
        }
        return params;
    }

    public Map<String, String> parseQueryParams(HttpServletRequest request, HttpServletResponse response, String[] params) throws IOException {
        String[] queryParams = request
            .getQueryString()
            .split("&");
        Map<String,String> mapParams = Arrays.stream(queryParams)
                .map(string -> string.split("="))
                .collect(Collectors.toMap((str)-> str[0], (str)-> str[1]));


        for(String param : params) {
            if(!mapParams.containsKey(param)){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }        }
        return mapParams;
    }
}
