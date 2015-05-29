package application;

import com.victorkzk.furniture.Furniture;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AbstractElementAdapter implements JsonSerializer<Furniture>, JsonDeserializer<Furniture> {
    @Override
    public JsonElement serialize(Furniture src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }


    @Override
    public Furniture deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");
        ArrayList<Class> plugins = Controller.getLoadedPlugins();
        for (int i = 0; i < plugins.size(); i++) {
            if (plugins.get(i).getName().equals(type)) {
                return context.deserialize(element, plugins.get(i));
            }
        }

        try {
            String thepackage = "furniture.";
            return context.deserialize(element, Class.forName(thepackage + type));
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + type, cnfe);
        }
    }
}
