package model;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PatientListTest {
    private PatientList patients;

    private Patient patient0;
    private Patient patient1;
    private Patient patient2;
    private Patient patient3;

    @BeforeEach
    public void runBefore() {
        patients = new PatientList();

        patient0 = new Patient("Patient Zero", "11/11/1999", "1091239501");
        patient1 = new Patient("", "3/16/2020", "2310529181");
        patient2 = new Patient("", "2/14/1920", "1231859234");
        patient3 = new Patient("", "4/14/1953", "1819230518");
    }

    @Test
    public void testConstructor() {
        assertEquals(0, patients.getPatients().size());
    }

    @Test
    public void testAddSinglePatient() {
        boolean add1 = patients.addPatient(patient0);
        assertEquals(1, patients.getPatients().size());
        assertEquals(patient0, patients.getPatients().get(0));
        assertTrue(add1);
    }

    @Test
    public void testAddMultiplePatients() {
        boolean add1 = patients.addPatient(patient0);
        boolean add2 = patients.addPatient(patient1);
        boolean add3 = patients.addPatient(patient2);

        List<Patient> listPatients = patients.getPatients();

        assertEquals(3, listPatients.size());
        assertEquals(patient0, listPatients.get(0));
        assertEquals(patient1, listPatients.get(1));
        assertEquals(patient2, listPatients.get(2));

        assertTrue(add1);
        assertTrue(add2);
        assertTrue(add3);
    }

    @Test
    public void testAddSamePatientMultipleTimes() {
        boolean add1 = patients.addPatient(patient0);
        boolean add2 = patients.addPatient(patient0);
        boolean add3 = patients.addPatient(patient0);

        List<Patient> listPatients = patients.getPatients();

        assertEquals(1, listPatients.size());
        assertEquals(patient0, listPatients.get(0));
        assertTrue(add1);
        assertFalse(add2);
        assertFalse(add3);
    }

    @Test
    public void testRemovePatientNotInList() {
        patients.addPatient(patient0);
        patients.addPatient(patient1);
        patients.addPatient(patient2);

        boolean res = patients.removePatient("1231823912");

        List<Patient> listPatients = patients.getPatients();

        assertEquals(3, listPatients.size());
        assertEquals(patient0, listPatients.get(0));
        assertEquals(patient1, listPatients.get(1));
        assertEquals(patient2, listPatients.get(2));
        assertFalse(res);
    }

    @Test
    public void testRemovePatientInList() {
        patients.addPatient(patient0);
        patients.addPatient(patient1);
        patients.addPatient(patient2);

        boolean res = patients.removePatient(patient0.getPHN());

        List<Patient> listPatients = patients.getPatients();

        assertEquals(2, listPatients.size());
        assertEquals(patient1, listPatients.get(0));
        assertEquals(patient2, listPatients.get(1));
        assertTrue(res);
    }

    @Test
    public void testRemoveMultiplePatientsInList() {
        patients.addPatient(patient0);
        patients.addPatient(patient1);
        patients.addPatient(patient2);

        boolean res1 = patients.removePatient(patient0.getPHN());
        boolean res2 = patients.removePatient(patient2.getPHN());

        List<Patient> listPatients = patients.getPatients();

        assertEquals(1, listPatients.size());
        assertEquals(patient1, listPatients.get(0));
        assertTrue(res1);
        assertTrue(res2);
    }

    @Test
    public void testIsPatientRegisteredWithNoMatch() {
        patients.addPatient(patient0);
        patients.addPatient(patient1);
        patients.addPatient(patient2);

        boolean res = patients.isPatientRegistered(patient3);
        assertFalse(res);
    }

    @Test
    public void testIsPatientRegisteredWithMatch() {
        patients.addPatient(patient0);
        patients.addPatient(patient1);
        patients.addPatient(patient2);

        boolean res = patients.isPatientRegistered(patient0);
        assertTrue(res);

        boolean res2 = patients.isPatientRegistered(patient1);
        assertTrue(res2);

        boolean res3 = patients.isPatientRegistered(patient2);
        assertTrue(res3);
    }

    @Test
    public void testFindPatientWithNoMatchNonEmptyList() {
        patients.addPatient(patient0);
        patients.addPatient(patient1);
        patients.addPatient(patient2);

        Patient p = patients.findPatient("1236781023");
        assertNull(p);
    }

    @Test
    public void testFindPatientWithNoMatchEmptyList() {
        Patient p = patients.findPatient("1231231230");
        assertNull(p);
    }

    @Test
    public void testFindPatientWithMatch() {
        patients.addPatient(patient0);
        patients.addPatient(patient1);

        Patient p = patients.findPatient("1091239501");
        assertEquals(patient0, p);
    }
}
