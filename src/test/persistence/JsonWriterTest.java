package persistence;

import model.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Tests were derived from JsonSerializationDemo
public class JsonWriterTest {
    @Test
    public void testWriterInvalidFile() {
        try {
            Clinic testClinic = new Clinic(null, null);
            JsonWriter writer = new JsonWriter("./data/\0illegal:FileName.json");
            writer.saveClinicToJson(testClinic);
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testWriterEmptyClinic() {
        try {
            Clinic testClinic = new Clinic(null, null);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyClinic.json");
            writer.saveClinicToJson(testClinic);

            JsonReader reader = new JsonReader("./data/testWriterEmptyClinic.json");
            testClinic = reader.read();
            assertEquals(0, testClinic.getPatients().size());
            assertEquals(0, testClinic.getConditions().size());
        } catch (IOException e) {
            fail("This shouldn't be possible!");
        }
    }

    @Test
    public void testWriterNormalClinic() {
        try {
            Clinic testClinic = new Clinic(null, null);
            testClinic.addPatient(new Patient("Jeff Ho", "12/11/1999", "1234567890"));
            testClinic.addCondition(new Condition("Genetic Condition A", "ATGCATGC", 10));
            JsonWriter writer = new JsonWriter("./data/testWriterNormalClinic.json");
            writer.saveClinicToJson(testClinic);

            JsonReader reader = new JsonReader("./data/testWriterNormalClinic.json");
            testClinic = reader.read();

            List<Patient> patients = testClinic.getPatients();
            List<Condition> conditions = testClinic.getConditions();

            assertEquals(1, patients.size());
            assertEquals(1, conditions.size());

            checkPatient(patients.get(0), "Jeff Ho", "12/11/1999", "1234567890", 23, "", "");
            checkCondition(conditions.get(0), "Genetic Condition A", "ATGCATGC", 10);
        } catch (IOException e) {
            fail("This shouldn't be possible!");
        }
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
