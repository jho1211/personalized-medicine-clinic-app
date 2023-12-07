package persistence;

import model.Event;
import model.EventLog;
import org.json.JSONObject;

import model.Clinic;

import java.io.*;

// A writer that converts a Clinic to a JSON object and writes it to a local file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String fileName;

    // EFFECTS: Constructs writer with specified fileName
    public JsonWriter(String fileName) {
        this.fileName = fileName;
    }

    // MODIFIES: this
    // EFFECTS: Opens writer, writes and saves data to file, and then closes writer
    public void saveClinicToJson(Clinic clinic) throws IOException {
        open();
        write(clinic);
        close();
        EventLog.getInstance().logEvent(new Event("Saved the clinic data."));
    }

    // MODIFIES: this
    // EFFECTS: opens writer and throws FileNotFoundException if file can't be
    // accessed for writing
    private void open() throws IOException {
        writer = new PrintWriter(new File(fileName));
    }

    // MODIFIES: this
    // EFFECTS: Writes JSONified Clinic to file as string and saves it
    private void write(Clinic clinic) {
        String jsonObjStr = clinic.toJson().toString(TAB);
        writer.print(jsonObjStr);
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    private void close() {
        writer.close();
    }
}
