package anth123.top100itunesapps.webapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import anth123.top100itunesapps.AppEntry;

public class AppEntriesDeserializer implements JsonDeserializer<List<AppEntry>>{

    @Override
    public List<AppEntry> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        List<AppEntry> appEntryList = new ArrayList<AppEntry>();
        // get from the JSON response only what we need
        JsonArray entries = json.getAsJsonObject().getAsJsonObject("feed").getAsJsonArray("entry");
        for (JsonElement elementEntry : entries) {
            JsonObject object = elementEntry.getAsJsonObject();
            appEntryList.add(new AppEntry(
                        object
                            .getAsJsonObject("im:name")
                            .getAsJsonPrimitive("label")
                            .getAsString(),
                        object
                            .getAsJsonArray("im:image")
                            .get(2)
                            .getAsJsonObject()
                            .getAsJsonPrimitive("label")
                            .getAsString(),
                        object
                            .getAsJsonObject("im:price")
                            .getAsJsonPrimitive("label")
                            .getAsString()
                    )
            );
        }
        return appEntryList;
    }
}
