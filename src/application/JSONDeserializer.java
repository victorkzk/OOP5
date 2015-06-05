package application;

import com.google.gson.*;
import com.victorkzk.furniture.Deserializer;
import com.victorkzk.furniture.Furniture;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;


public class JSONDeserializer implements Deserializer {

    Set<Class> classList = new HashSet<Class>();

    @Override
    public void addClass(Class c) {
        classList.add(c);
    }

    @Override
    public List<Object> deserialize(String filePath) throws IOException {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Furniture.class, new AbstractElementAdapter());
        Furniture[] furnitureArray = gson.create().fromJson((new String(readAllBytes(get(filePath)))),
                Furniture[].class);
        return Arrays.asList(furnitureArray);
    }

    @Override
    public String getFileExtension() {
        return ".json";
    }

    private class AbstractElementAdapter implements JsonDeserializer<Furniture> {
        @Override
        public Furniture deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String type = jsonObject.get("type").getAsString();
            JsonElement element = jsonObject.get("properties");
            for (Class c : classList) {
                if (c.getSimpleName().equals(type)) {
                    return context.deserialize(element, c);
                }
            }
            throw new JsonParseException("Unknown element type: " + type);
        }
    }

}
