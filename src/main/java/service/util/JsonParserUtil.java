package service.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonParserUtil {

    public static JsonObject parseStringToJsonObject(String stringToParse) {
        JsonElement jsonParser = JsonParser.parseString(stringToParse);
        return jsonParser.getAsJsonObject();
    }

    public static JsonArray parseStringToJsonArray(String stringToParse) {
        JsonElement jsonParser = JsonParser.parseString(stringToParse);
        return jsonParser.getAsJsonArray();
    }
}
