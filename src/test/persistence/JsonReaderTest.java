package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {
    private Clinic testClinic;

    @BeforeEach
    public void runBefore() {
        testClinic = new Clinic(null, null);
    }

    @Test
    public void testReadInvalidFile() {
        JsonReader jsonReader = new JsonReader("./data/nonExistentFile.json");
        try {
            testClinic = jsonReader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass;
        }
        List<Patient> patients = testClinic.getPatients();
        List<Condition> conditions = testClinic.getConditions();

        assertEquals(0, patients.size());
        assertEquals(0, conditions.size());
    }

    @Test
    public void testReadEmptyJSON() {
        JsonReader jsonReader = new JsonReader("./data/testReaderEmptyClinic.json");
        try {
            testClinic = jsonReader.read();
        } catch (IOException e) {
            fail("Couldn't read the specified file.");
        }
        List<Patient> patients = testClinic.getPatients();
        List<Condition> conditions = testClinic.getConditions();

        assertEquals(0, patients.size());
        assertEquals(0, conditions.size());
    }

    @Test
    public void testReadNormalClinic() {
        JsonReader jsonReader = new JsonReader("./data/testReaderNormalClinic.json");
        try {
            testClinic = jsonReader.read();
        } catch (IOException e) {
            fail("Couldn't read the specified file.");
        }
        List<Patient> patients = testClinic.getPatients();
        List<Condition> conditions = testClinic.getConditions();

        assertEquals(3, patients.size());
        assertEquals(3, conditions.size());

        checkPatient(patients.get(0), "Jeff Ho", "11/11/1999", "1091239501", 23,
                "", "Patient is in satisfactory condition.");
        checkPatient(patients.get(1), "David Becker", "12/12/2001", "2310529181", 21,
                "", "Patient is in healthy condition.");
        checkPatient(patients.get(2), "Ryan Boe", "09/23/1950", "1618234512", 73,
                "", "Patient is in critical condition. Sent to hospital for observation.");

        checkCondition(conditions.get(0), "Genetic Condition A", "ACATGCATG", 10);
        checkCondition(conditions.get(1), "Genetic Condition B", "TGACGTTAA", 5);
        checkCondition(conditions.get(2), "Genetic Condition C", "ATATATATA", 7);
    }

    private void checkPatient(Patient patient, String name, String dob, String phn,
                              int age, String genome, String notes) {
        assertEquals(name, patient.getFullName());
        assertEquals(dob, patient.getDOB());
        assertEquals(phn, patient.getPHN());
        assertEquals(age, patient.getAge());
        assertEquals(notes, patient.getNotes());
        assertEquals(genome, patient.getGenome());
    }

    private void checkCondition(Condition condition, String condName, String seq, int chrNum) {
        assertEquals(condName, condition.getName());
        assertEquals(seq, condition.getSequence());
        assertEquals(chrNum, condition.getChromosomeNumber());
    }
}
