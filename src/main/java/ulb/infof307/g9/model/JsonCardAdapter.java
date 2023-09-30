package ulb.infof307.g9.model;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ulb.infof307.g9.model.cards.Card;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class use when we write a Card object in a JSON file (supports inheritance)
 * Otherwise, when reading from a JSON file, Gson will not know which class to use
 * to create the object, and will use the default class, Card.
 */
public class JsonCardAdapter extends TypeAdapter<List<Card>> {
    private static final String CLASSNAME = "CLASSNAME_DO_NOT_USE_AS_AN_ATTRIBUTE_IN_YOUR_CLASSES";
    /**
     * Writes a List of Card objects to a JSON file. Supports inheritance
     * by adding the class name of the object to the JSON object.
     *
     * @param out the JSON file to write to
     * @param cards the Java object to write. May be null.
     */
    @Override
    public void write(JsonWriter out, List<Card> cards) throws IOException {
        Gson gson = new Gson();
        out.beginArray(); // begins an array in the JSON file
        for (Card card : cards) {
            // transform the object into a JSON object
            JsonElement jsonElement = gson.toJsonTree(card);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            // add the class name of the card to the JSON object
            jsonObject.addProperty(CLASSNAME, card.getClass().getName());

            // write the JSON object to the JSON file
            gson.toJson(jsonObject, out);
        }
        out.endArray(); // ends the array in the JSON file
    }

    /**
     * Reads a List of Card objects from a JSON file. Supports inheritance
     * by reading the class name of the object from the JSON object to know
     * which class to use to create the object.
     *
     * @param in the JSON file to read from
     * @return the converted Java object. May be null.
     */
    @Override
    public List<Card> read(JsonReader in) throws IOException {
        List<Card> cards = new ArrayList<>();
        Gson gson = new Gson();

        in.beginArray(); // begin reading the array in the JSON file
        while (in.hasNext()) {
            // read the JSON object
            JsonObject jsonObject = JsonParser.parseReader(in).getAsJsonObject();

            // get the class name of the card as a string
            JsonPrimitive classNamePrimitive = (JsonPrimitive) jsonObject.get(CLASSNAME);
            String className = classNamePrimitive.getAsString();

            // try to find the class corresponding to the class name
            Class<?> clazz;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e.getMessage());
            }

            // add the card to the list
            cards.add((Card) gson.fromJson(jsonObject, clazz));
        }
        in.endArray();
        return cards;
    }
}