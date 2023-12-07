package persistence;

import org.json.JSONObject;

// Taken from JsonSerializableDemo
public interface Writable {
    // EFFECTS: Returns as JSON object
    JSONObject toJson();
}
