package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

// A reader that reads Clinic from JSON data stored in file
// Based on JsonSerializationDemo
public class JsonReader {
    private String fileName;

    // EFFECTS: Constructs file reader to read fileName
    public JsonReader(String fileName) {
        this.fileName = fileName;
    }

    // EFFECTS: reads Clinic data from file and returns it
    // throws IOException if error occurs while reading data from file
    public Clinic read() throws IOException {
        String data = readFile(fileName);
        JSONObject jsonObj = new JSONObject(data);
        EventLog.getInstance().logEvent(new Event("Loaded the last saved clinic data."));
        return parseClinic(jsonObj);
    }

    // EFFECTS: Reads file as string and returns it
    private String readFile(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();

        File file = new File(fileName);
        Scanner fileReader = new Scanner(file);

        while (fileReader.hasNextLine()) {
            content.append(fileReader.nextLine());
        }

        fileReader.close();

        return content.toString();
    }

    // EFFECTS: Parses Clinic from JSON objects and returns it
    private Clinic parseClinic(JSONObject jsonObj) {
        PatientList patients = parsePatients(jsonObj);
        ConditionList conditions = parseConditions(jsonObj);
        return new Clinic(patients, conditions);
    }

    // EFFECTS: Parses Patients from JSON objects and returns PatientList
    private PatientList parsePatients(JSONObject jsonObj) {
        JSONArray jsonArr = jsonObj.getJSONArray("patients");
        PatientList patients = new PatientList();

        for (Object obj : jsonArr) {
            JSONObject nextPatient = (JSONObject) obj;
            Patient patient = parsePatient(nextPatient);
            patients.addPatient(patient);
        }
        return patients;
    }

    // EFFECTS: Parses Conditions from JSON objects and returns ConditionList
    private ConditionList parseConditions(JSONObject jsonObj) {
        JSONArray jsonArr = jsonObj.getJSONArray("conditions");
        ConditionList conditions = new ConditionList();

        for (Object obj : jsonArr) {
            JSONObject nextCondition = (JSONObject) obj;
            Condition cond = parseCondition(nextCondition);
            conditions.addCondition(cond);
        }
        return conditions;
    }

    // EFFECTS: Parses Patient from JSON object and returns Patient
    private Patient parsePatient(JSONObject jsonObj) {
        String name = jsonObj.getString("name");
        String dob = jsonObj.getString("dob");
        String phn = jsonObj.getString("phn");
        String notes = jsonObj.getString("notes");

        Patient patient = new Patient(name, dob, phn);
        patient.updateNotes(notes);

        return patient;
    }

    // EFFECTS: Parses Condition from JSON object and returns Condition
    private Condition parseCondition(JSONObject jsonObj) {
        String name = jsonObj.getString("name");
        String seq = jsonObj.getString("seq");
        int chrNum = jsonObj.getInt("chrNum");

        return new Condition(name, seq, chrNum);
    }
}
