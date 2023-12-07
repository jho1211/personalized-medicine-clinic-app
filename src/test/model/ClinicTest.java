package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClinicTest {
    private Clinic testClinic;
    private Patient testPatient;
    private Patient testPatient2;
    private Condition conditionA;
    private Condition conditionB;

    @BeforeEach
    public void runBefore() {
        testClinic = new Clinic(null, null);

        testPatient = new Patient("Patient Zero", "11/11/1999", "1091239501");
        testPatient2 = new Patient("", "3/16/2020", "2310529181");

        conditionA = new Condition("Genetic Condition A", "ATGCAATGC", 10);
        conditionB = new Condition("Genetic Condition B", "TGCCATGGC", 5);

    }

    @Test
    public void testConstructorWithNullInput() {
        testClinic = new Clinic(null, null);
        assertEquals(0, testClinic.getConditions().size());
        assertEquals(0, testClinic.getPatients().size());
    }

    @Test
    public void testConstructorWithExistingData() {
        PatientList patients = new PatientList();
        patients.addPatient(testPatient);
        ConditionList conditions = new ConditionList();
        conditions.addCondition(conditionA);

        testClinic = new Clinic(patients, conditions);
        assertEquals(1, testClinic.getConditions().size());
        assertEquals(1, testClinic.getPatients().size());
    }

    @Test
    public void testAddSinglePatient() {
        boolean res = testClinic.addPatient(testPatient);
        assertTrue(res);

        List<Patient> patients = testClinic.getPatients();
        assertEquals(1, patients.size());

        Patient patient = patients.get(0);
        assertEquals(testPatient, patient);
    }

    @Test
    public void testAddMultiplePatients() {
        testClinic.addPatient(testPatient);
        testClinic.addPatient(testPatient2);

        List<Patient> patients = testClinic.getPatients();
        assertEquals(2, patients.size());

        Patient patient1 = patients.get(0);
        Patient patient2 = patients.get(1);

        assertEquals(testPatient, patient1);
        assertEquals(testPatient2, patient2);
    }

    @Test
    public void testAddExistingPatient() {
        boolean res = testClinic.addPatient(testPatient);
        boolean res2 = testClinic.addPatient(testPatient);

        List<Patient> patients = testClinic.getPatients();
        assertEquals(1, patients.size());

        Patient patient1 = patients.get(0);
        assertEquals(testPatient, patient1);

        assertTrue(res);
        assertFalse(res2);
    }

    @Test
    public void testRemovePatient() {
        testClinic.addPatient(testPatient);
        boolean res = testClinic.removePatient("1091239501");
        assertTrue(res);


        List<Patient> patients = testClinic.getPatients();
        assertEquals(0, patients.size());
    }

    @Test
    public void testRemoveMultiplePatients() {
        testClinic.addPatient(testPatient);
        testClinic.addPatient(testPatient2);
        boolean res1 = testClinic.removePatient("1091239501");
        boolean res2 = testClinic.removePatient("2310529181");

        assertTrue(res1);
        assertTrue(res2);

        List<Patient> patients = testClinic.getPatients();
        assertEquals(0, patients.size());
    }

    @Test
    public void testRemoveNonExistentPatient() {
        testClinic.addPatient(testPatient);
        boolean res1 = testClinic.removePatient("2310529181");
        assertFalse(res1);

        List<Patient> patients = testClinic.getPatients();
        assertEquals(1, patients.size());
        assertEquals(testPatient, patients.get(0));
    }

    @Test
    public void testAddMultipleConditionsNotInList() {
        boolean res1 = testClinic.addCondition(conditionA);
        boolean res2 = testClinic.addCondition(conditionB);

        List<Condition> conditions = testClinic.getConditions();

        assertTrue(res1);
        assertTrue(res2);

        assertEquals(2, conditions.size());
        assertEquals(conditionA, conditions.get(0));
        assertEquals(conditionB, conditions.get(1));
    }

    @Test
    public void testAddMultipleConditionsSomeInList() {
        boolean res1 = testClinic.addCondition(conditionA);
        boolean res2 = testClinic.addCondition(conditionB);
        boolean res3 = testClinic.addCondition(conditionB);

        List<Condition> conditions = testClinic.getConditions();

        assertTrue(res1);
        assertTrue(res2);
        assertFalse(res3);

        assertEquals(2, conditions.size());
        assertEquals(conditionA, conditions.get(0));
        assertEquals(conditionB, conditions.get(1));
    }

    @Test
    public void testRemoveConditionNotInList() {
        testClinic.addCondition(conditionA);
        testClinic.addCondition(conditionB);

        boolean res = testClinic.removeCondition("Genetic Condition C");

        List<Condition> conditions = testClinic.getConditions();

        assertEquals(2, conditions.size());
        assertEquals(conditionA, conditions.get(0));
        assertEquals(conditionB, conditions.get(1));
        assertFalse(res);
    }

    @Test
    public void testRemoveMultipleConditions() {
        testClinic.addCondition(conditionA);
        testClinic.addCondition(conditionB);

        boolean res1 = testClinic.removeCondition("Genetic Condition A");
        boolean res2 = testClinic.removeCondition("Genetic Condition B");

        List<Condition> conditions = testClinic.getConditions();

        assertEquals(0, conditions.size());
        assertTrue(res1);
        assertTrue(res2);
    }

    @Test
    public void testFindPatientExistAndNotExist() {
        testClinic.addPatient(testPatient);
        Patient patient = testClinic.findPatient("1234567890");
        assertNull(patient);
        patient = testClinic.findPatient("1091239501");
        assertEquals(testPatient, patient);
    }
}
