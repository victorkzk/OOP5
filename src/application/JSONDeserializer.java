package application;

import com.google.gson.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.victorkzk.furniture.Deserializer;
import com.victorkzk.furniture.Furniture;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;


public class JSONDeserializer implements Deserializer {

    Set<Class> classes = new HashSet<Class>();

    @Override
    public void addClass(Class c) {
        classes.add(c);
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
            for (Class c : classes) {
                if (c.getSimpleName().equals(type)) {
                    return context.deserialize(element, c);
                }
            }
            throw new JsonParseException("Unknown element type: " + type);
        }
    }

}
