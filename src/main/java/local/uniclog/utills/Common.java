package local.uniclog.utills;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@Slf4j
public class Common {
    public static final String CONFIG_LIST_JSON = "config_list.json";

    public <T> void write(T object) {
        try (var writer = new FileWriter(CONFIG_LIST_JSON)) {
            writer.write(new Gson().toJson(object));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public <T> T read(Type rawType, Type type) {
        try (var reader = new FileReader(CONFIG_LIST_JSON)) {
            var token = TypeToken.getParameterized(rawType, type).getType();
            return new Gson().fromJson(reader, token);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
