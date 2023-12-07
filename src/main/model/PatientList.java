package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;
import persistence.WritableArray;

import java.util.*;

/*
Stores a list of patients who are registered with the clinic
toJson method is derived from JsonSeralizationDemo
 */
public class PatientList implements WritableArray {
    private List<Patient> patients;

    // EFFECTS: Constructs a new PatientList with an empty list of patients.
    public PatientList() {
        this.patients = new ArrayList<Patient>();
    }

    // MODIFIES: this
    // EFFECTS: Adds patient to the list if not already present, otherwise do nothing
    // Returns true if patient was added, false otherwise.
    public boolean addPatient(Patient patient) {
        if (!isPatientRegistered(patient)) {
            this.patients.add(patient);
            EventLog.getInstance().logEvent(new Event("Added a new patient, "
                    + patient.getFullName() + ", to the list of patients."));
            return true;
        }

        return false;
    }

    // MODIFIES: this
    // EFFECTS: Removes patient with specified PHN from the list if present, otherwise do nothing
    // Returns true if patient was found and removed, false otherwise
    public boolean removePatient(String phn) {
        List<Patient> newPatients = new ArrayList<Patient>();
        boolean found = false;

        for (Patient p : this.patients) {
            if (!p.getPHN().equals(phn)) {
                newPatients.add(p);
            } else {
                found = true;
                EventLog.getInstance().logEvent(new Event("Removed a patient with PHN, "
                        + phn + ", from the list of patients."));
            }
        }

        this.patients = newPatients;
        return found;
    }

    // EFFECTS: Returns the patient with the specified PHN if they exist, otherwise return null
    public Patient findPatient(String phn) {
        for (Patient p : this.patients) {
            if (p.getPHN().equals(phn)) {
                return p;
            }
        }

        return null;
    }

    // EFFECTS: Returns true if the patient is present in the list of patients and false otherwise
    public boolean isPatientRegistered(Patient patient) {
        for (Patient p : this.patients) {
            if (p.getPHN().equals(patient.getPHN())) {
                return true;
            }
        }

        return false;
    }

    // EFFECTS: Returns the list of patients registered so far
    public List<Patient> getPatients() {
        return this.patients;
    }

    // EFFECTS: Converts PatientList object to a JSONObject and returns it
    @Override
    public JSONArray toJsonArray() {
        JSONArray jsonArr = new JSONArray();

        for (Patient p : patients) {
            jsonArr.put(p.toJson());
        }

        return jsonArr;
    }
}
