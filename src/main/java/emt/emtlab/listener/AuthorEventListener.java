package emt.emtlab.listener;

import emt.emtlab.events.AuthorChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorEventListener {
    private final JdbcTemplate jdbcTemplate;

    @EventListener
    public void handleAuthorChangeEvent(AuthorChangeEvent event) {
        jdbcTemplate.execute("REFRESH MATERIALIZED VIEW author_counts_by_country");
    }
}