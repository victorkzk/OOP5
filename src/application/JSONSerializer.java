package application;

import com.google.gson.*;
import com.victorkzk.furniture.Furniture;
import com.victorkzk.furniture.Serializer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.*;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

public class JSONSerializer implements Serializer {

    public void serialize(String filePath, List<Object> objectList) throws FileNotFoundException{
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Furniture.class, new AbstractElementAdapter());
        String json = gson.setPrettyPrinting().create().toJson(objectList.toArray(), Furniture[].class);
        PrintWriter writer = new PrintWriter(filePath);
        writer.write(json);
        writer.close();
    }

    public String getFileExtension() {
        return ".json";
    }

    private class AbstractElementAdapter implements JsonSerializer<Furniture> {
        @Override
        public JsonElement serialize(Furniture src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
            result.add("properties", context.serialize(src, src.getClass()));
            return result;
        }
    }

}
