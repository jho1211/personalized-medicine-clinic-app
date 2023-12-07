package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {
    private Patient testPatient;
    private Patient testPatient2;
    private Patient testPatient3;
    private Condition conditionA;
    private Condition conditionB;

    @BeforeEach
    public void runBefore() {
        testPatient = new Patient("Patient Zero", "11/11/1999", "1091239501");
        testPatient2 = new Patient("", "3/16/2020", "2310529181");
        testPatient3 = new Patient("", "09/23/1950", "2310529181");

        conditionA = new Condition("Genetic Condition A", "ATGCAATGC", 10);
        conditionB = new Condition("Genetic Condition B", "TGCCATGGC", 5);

        EventLog.getInstance().clear();
    }

    @Test
    public void testConstructor() {
        assertEquals("Patient Zero", testPatient.getFullName());
        assertEquals("11/11/1999", testPatient.getDOB());
        assertEquals("1091239501", testPatient.getPHN());
        assertEquals(24, testPatient.getAge());
        assertEquals("", testPatient.getGenome());
        assertEquals("", testPatient.getNotes());
    }

    @Test
    public void testUpdateAge() {
        testPatient.updateAge();
        assertEquals(24, testPatient.getAge());

        testPatient2.updateAge();
        assertEquals(3, testPatient2.getAge());

        testPatient3.updateAge();
        assertEquals(73, testPatient3.getAge());
    }

    @Test
    public void testUpdateNotes() {
        testPatient.updateNotes("This patient is new to the clinic.");
        assertEquals("This patient is new to the clinic.", testPatient.getNotes());

        testPatient2.updateNotes("Hello world.");
        assertEquals("Hello world.", testPatient2.getNotes());
    }

    @Test
    public void testDiagnosePatientWithCondition() {
        testPatient.updateGenome("GTACATGCAATGCATGGG");
        boolean result = testPatient.diagnose(conditionA);
        assertTrue(result);
    }

    @Test
    public void testDiagnosePatientWithNoCondition() {
        testPatient.updateGenome("GTACATGCAATGCATGGG");
        boolean result = testPatient.diagnose(conditionB);
        assertFalse(result);
    }

    @Test
    public void testDiagnosePatientWithCloseMatch() {
        testPatient.updateGenome("GTACATGCGATGCATGGG");
        boolean result = testPatient.diagnose(conditionA);
        assertFalse(result);
    }

    @Test
    public void testReadGenomeMissingFile() {
        testPatient.readGenome();
        assertEquals("", testPatient.getGenome());
    }

    @Test
    public void testReadGenomeFromFile() {
        testPatient2.readGenome();
        assertEquals("TGACATTGGCCAAACATGCATG", testPatient2.getGenome());
        assertTrue(getEventLog().contains("Fetched the genome for the patient with PHN, "
                + testPatient2.getPHN() + " from the data folder."));
    }

    @Test
    public void testHasNoGenome() {
        testPatient.readGenome();
        assertTrue(testPatient.hasNoGenome());

        testPatient2.readGenome();
        assertFalse(testPatient2.hasNoGenome());
    }

    @Test
    public void testGetDiagnosisReportWithGenome() {
        testPatient.updateGenome("GTACATGCAATGCATGGG");
        assertEquals("Diagnosis for Genetic Condition A\n"
                + "Patient Genome: ATGCAATGC\n"
                + "Condition Seq:  ATGCAATGC\n"
                + "(Similarity: 100%)\n\n",
                testPatient.getDiagnosisReport(conditionA));
    }

    @Test
    public void testGetFullDiagnosisWithGenome() {
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(conditionA);
        conditionList.add(conditionB);
        testPatient.updateGenome("GTACATGCAATGCATGGG");
        assertEquals("Diagnosis for Genetic Condition A\n"
                        + "Patient Genome: ATGCAATGC\n"
                        + "Condition Seq:  ATGCAATGC\n"
                        + "(Similarity: 100%)\n\nDiagnosis for Genetic Condition B\n" +
                        "Patient Genome: TGC-ATG--\n" +
                        "Condition Seq:  TGCCATGGC\n" +
                        "(Similarity: 67%)\n\n",
                testPatient.getFullDiagnosisReport(conditionList));
        assertTrue(getEventLog().contains("Retrieved a full diagnosis report for patient with PHN, "
                + testPatient.getPHN()));
    }

    @Test
    public void testGetFullDiagnosisWithNoGenome() {
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(conditionA);
        conditionList.add(conditionB);
        assertEquals("", testPatient.getFullDiagnosisReport(conditionList));
    }

    @Test
    public void testGetDiagnosisReportWithNoGenome() {
        assertEquals("",
                testPatient.getDiagnosisReport(conditionA));
    }

    @Test
    public void testToStringMethod() {
        assertEquals("Patient Zero (PHN: 1091239501)", testPatient.toString());
    }

    @Test
    public void testAllPatientInfo() {
        assertEquals("Full Name: Patient Zero\n" +
                "PHN: 1091239501\n" +
                "Age: 24\n" +
                "Date of Birth: 11/11/1999\n" +
                "Notes: ", testPatient.getAllPatientInfo());
        assertTrue(getEventLog().contains("Retrieved information about patient with PHN: " + testPatient.getPHN()));
    }

    // EFFECTS: Returns a list of all the event descriptions in EventLog
    public ArrayList<String> getEventLog() {
        ArrayList<String> events = new ArrayList<>();
        for (Event e : EventLog.getInstance()) {
            events.add(e.getDescription());
        }

        return events;
    }
}