package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {
    private Event evt;
    private Event evt2;
    private Date date;

    @BeforeEach
    public void runBefore() {
        evt = new Event("Added a patient to clinic.");
        evt2 = new Event("Added a patient to clinic.");
        date = Calendar.getInstance().getTime();
    }

    @Test
    public void testConstructor() {
        assertEquals("Added a patient to clinic.", evt.getDescription());
        assertEquals(date, evt.getDate());
    }

    @Test
    public void testToString() {
        assertEquals(date.toString() + "\n" + evt.getDescription(), evt.toString());
    }

    @Test
    public void testEquality() {
        assertTrue(evt.equals(evt));
        assertTrue(evt.equals(evt2));
        assertFalse(evt.equals(date));
        assertFalse(evt.equals(null));
    }

    @Test
    public void testHashcode() {
        assertEquals(13 * evt.getDate().hashCode() + evt.getDescription().hashCode(), evt.hashCode());
    }
}
