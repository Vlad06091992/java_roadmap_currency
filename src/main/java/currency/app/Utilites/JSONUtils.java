package currency.app.Utilites;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

public class JSONUtils {

   public static Utils utils = new Utils();

    public static <T> String getSimpleJson(T t) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(t);
    }


    public static <T> String getSimpleJsonWithoutId(T t) {
        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                return Objects.equals(fieldAttributes.getName(), "id");
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        }).setPrettyPrinting().create();
        return gson.toJson(t);
    }

    public static <T> String getListToJson(List<T> t) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(t);
    }

    public static void printError(PrintWriter out, String message) {
        out.print(JSONUtils.getSimpleJson(utils.getError(message)));
    }
}