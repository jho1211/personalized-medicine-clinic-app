package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// A clinic that has a list of patients and a list of conditions
public class Clinic implements Writable {
    private PatientList patients;
    private ConditionList conditions;

    // EFFECTS: Constructs a clinic with provided patient list and condition list
    // or the patients and conditions is empty if null is provided;
    public Clinic(PatientList patients, ConditionList conditions) {
        if (patients == null) {
            this.patients = new PatientList();
        } else {
            this.patients = patients;
        }

        if (conditions == null) {
            this.conditions = new ConditionList();
        } else {
            this.conditions = conditions;
        }
    }

    // MODIFIES: this
    // EFFECTS: Adds the patient to the PatientList if they aren't already present
    // Returns True if successful and False otherwise
    public boolean addPatient(Patient patient) {
        return patients.addPatient(patient);
    }

    // MODIFIES: this
    // EFFECTS: Removes the patient with specified PHN from the PatientList if they exist
    // Returns True if successful and False otherwise
    public boolean removePatient(String phn) {
        return patients.removePatient(phn);
    }

    // MODIFIES: this
    // EFFECTS: Adds the condition to the ConditionList if it doesn't already exist
    // Returns true if successful and false otherwise
    public boolean addCondition(Condition condition) {
        return conditions.addCondition(condition);
    }

    // MODIFIES: this
    // EFFECTS: Removes the condition with specified name from the ConditionList if it exists
    // Returns true if successful and false otherwise
    public boolean removeCondition(String condName) {
        return conditions.removeCondition(condName);
    }

    // EFFECTS: Returns the patient with the specified PHN if they exist, otherwise return null
    public Patient findPatient(String phn) {
        return patients.findPatient(phn);
    }

    // EFFECTS: Returns the list of patients
    public List<Patient> getPatients() {
        return this.patients.getPatients();
    }

    // EFFECTS: Returns the list of genetic conditions
    public List<Condition> getConditions() {
        return this.conditions.getConditions();
    }

    @Override
    // EFFECTS: Converts Clinic to JSON object and returns it.
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("patients", patients.toJsonArray());
        json.put("conditions", conditions.toJsonArray());

        return json;
    }
}
