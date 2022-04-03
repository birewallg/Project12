package local.uniclog.utils.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;

@Slf4j
public class GsonObjectWrapper {
    public static final String CONFIG_LIST_JSON = "config_list.json";

    private <T> Gson getGsonObject(Type typeOfObject) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(typeOfObject, new GsonAdapter<T>());
        return builder.create();
    }

    public <T> void writeContainer(T object, Type typeOfObject) {
        try (var writer = new FileWriter(CONFIG_LIST_JSON)) {
            writer.write(getGsonObject(typeOfObject).toJson(object));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public <T> T readContainer(Type typeOfObject, Class<T> typeOfContainer) {
        try (var reader = new FileReader(CONFIG_LIST_JSON)) {
            return getGsonObject(typeOfObject).fromJson(reader, typeOfContainer);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
