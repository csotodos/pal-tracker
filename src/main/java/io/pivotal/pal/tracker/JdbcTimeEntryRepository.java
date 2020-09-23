package io.pivotal.pal.tracker;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );

    private final ResultSetExtractor<TimeEntry> extractor =
            (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;

    public JdbcTimeEntryRepository(MysqlDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

//        this.jdbcTemplate.update("INSERT INTO time_entries (project_id, user_id, date, hours)" +
//                " VALUES("+ any.getProjectId() + ", " + any.getUserId() +
//                ", " + any.getDate() + ", " + any.getHours() + ")", keyHolder);

        this.jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement("INSERT INTO time_entries (project_id, user_id, date, hours) " +
                            "VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            statement.setDate(3, Date.valueOf(timeEntry.getDate()));
            statement.setInt(4, timeEntry.getHours());

            return statement;
        }, keyHolder);

        return this.find(keyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry delete(long timeEntryId) {
        TimeEntry deletedEntry = this.find(timeEntryId);
        this.jdbcTemplate.update("DELETE FROM time_entries WHERE id = ?", timeEntryId);

        return deletedEntry;
    }

    @Override
    public TimeEntry update(long timeEntryId, TimeEntry updatedEntry) {
        this.jdbcTemplate.update("UPDATE time_entries " +
                "SET project_id = ?, user_id = ?, date = ?, hours = ? " +
                "WHERE id = ?",
                updatedEntry.getProjectId(), updatedEntry.getUserId(), updatedEntry.getDate(), updatedEntry.getHours(), timeEntryId);

        return this.find(timeEntryId);
    }

    @Override
    public List<TimeEntry> list() {
        return this.jdbcTemplate.query("SELECT id, project_id, user_id, date, hours" +
                " FROM time_entries", mapper);
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        List<TimeEntry> resultList = this.jdbcTemplate.query("SELECT id, project_id, user_id, date, hours" +
                " FROM time_entries WHERE id = ?", new Object[]{timeEntryId}, mapper);

        return resultList.isEmpty()?null:resultList.get(0);
    }
}
