package emt.emtlab.events;

import emt.emtlab.services.domain.model.Author;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AuthorChangeEvent extends ApplicationEvent {
    private final Author author;
    private final String action;

    public AuthorChangeEvent(Object source, Author author, String action) {
        super(source);
        this.author = author;
        this.action = action;
    }
}