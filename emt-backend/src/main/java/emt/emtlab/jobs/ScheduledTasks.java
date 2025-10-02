package emt.emtlab.jobs;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final JdbcTemplate jdbcTemplate;

    @Scheduled(cron = "0 0 * * * *") // Run every hour
    public void refreshBooksByAuthorView() {
        jdbcTemplate.execute("REFRESH MATERIALIZED VIEW book_counts_by_author");
    }

    @Scheduled(cron = "0 0 * * * *")
    public void refreshAuthorsByCountryView() {
        jdbcTemplate.execute("REFRESH MATERIALIZED VIEW author_counts_by_country");
    }
}