package local.uniclog.utils.gson;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;

@Slf4j
public class GsonAdapter<T> implements JsonDeserializer<T>, JsonSerializer<T> {

    private static final String CLASS_NAME = "CLASS_NAME";
    private static final String DATA = "DATA";

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonObject.get(CLASS_NAME);
        String className = jsonPrimitive.getAsString();
        Class<?> classForName;
        try {
            classForName = Class.forName(className);
            return context.deserialize(jsonObject.get(DATA), classForName);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
            throw new JsonParseException(e.getMessage());
        }
    }

    @Override
    public JsonElement serialize(T object, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(CLASS_NAME, object.getClass().getName());
        jsonObject.add(DATA, context.serialize(object));
        return jsonObject;
    }
}