package io.pivotal.pal.tracker;

import java.util.*;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private long idCounter;
    final private Map<Long,TimeEntry> inMemoryRepo;

    public InMemoryTimeEntryRepository() {
        this.inMemoryRepo = new HashMap<Long,TimeEntry>();
        this.idCounter = 1;
    }

    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(this.idCounter);
        this.inMemoryRepo.put(idCounter++, timeEntry);

        return timeEntry;
    }

    public TimeEntry find(long id) {
        return this.inMemoryRepo.get(id);
    }

    public List<TimeEntry> list() {
        return new ArrayList<TimeEntry>(this.inMemoryRepo.values());
    }

    public TimeEntry delete(long id) {
        return this.inMemoryRepo.remove(id);
    }

    public TimeEntry update(long l, TimeEntry timeEntry) {
        timeEntry.setId(l);
        TimeEntry checkReplace = this.inMemoryRepo.replace(l,timeEntry);
        return checkReplace==null?null:timeEntry;
    }
}
