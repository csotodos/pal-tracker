package io.pivotal.pal.tracker;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TimeEntryRepository {

    public TimeEntry create(TimeEntry any);

    public TimeEntry delete(long timeEntryId);

    public TimeEntry update(long timeEntryId, TimeEntry expected);

    public List<TimeEntry> list();

    public TimeEntry find(long timeEntryId);
}
