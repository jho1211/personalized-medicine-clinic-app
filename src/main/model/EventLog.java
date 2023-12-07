package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Represents a log of Clinic events.
 * We use the Singleton Design Pattern to ensure that there is only
 * one EventLog in the system and that the system has global access
 * to the single instance of the EventLog.
 * Copied from the AlarmSystem application
 */
public class EventLog implements Iterable<Event> {
    /**
     * the only EventLog in the system (Singleton Design Pattern)
     */
    private static EventLog theLog;
    private Collection<Event> events;

    // EFFECTS: Creates a new EventLog with no events added yet
    private EventLog() {
        events = new ArrayList<Event>();
    }

    // EFFECTS: Returns the instance of EventLog and creates one
    // if it doesn't exist.
    public static EventLog getInstance() {
        if (theLog == null) {
            theLog = new EventLog();
        }

        return theLog;
    }

    // MODIFIES: this
    // EFFECTS: Adds the event to the list
    public void logEvent(Event e) {
        events.add(e);
    }

    // MODIFIES: this
    // EFFECTS: Clears the events in the list and logs this event
    public void clear() {
        events.clear();
        logEvent(new Event("Event log cleared."));
    }

    // EFFECTS: Returns the iterator
    @Override
    public Iterator<Event> iterator() {
        return events.iterator();
    }
}
