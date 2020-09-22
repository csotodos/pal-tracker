package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {
    private final TimeEntryRepository repo;

    public TimeEntryController(TimeEntryRepository repo) {
        this.repo = repo;
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        TimeEntry responseEntry = this.repo.delete(id);
        return new ResponseEntity(responseEntry, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity update(@PathVariable long id, @RequestBody TimeEntry timeEntry) {
        TimeEntry responseEntry = this.repo.update(id, timeEntry);
        return new ResponseEntity(responseEntry, responseEntry==null?HttpStatus.NOT_FOUND:HttpStatus.OK);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        TimeEntry responseEntry = this.repo.find(id);
        return new ResponseEntity<>(responseEntry,responseEntry==null?HttpStatus.NOT_FOUND:HttpStatus.OK);
    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntry) {
        return new ResponseEntity(this.repo.create(timeEntry), HttpStatus.CREATED);
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        return new ResponseEntity<>(this.repo.list(), HttpStatus.OK);
    }
}
