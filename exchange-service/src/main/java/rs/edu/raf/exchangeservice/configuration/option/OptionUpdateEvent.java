package rs.edu.raf.exchangeservice.configuration.option;

import org.springframework.context.ApplicationEvent;
import rs.edu.raf.exchangeservice.domain.model.listing.Option;

public class OptionUpdateEvent extends ApplicationEvent {

    private final Option option;

    public OptionUpdateEvent(Object source, Option option) {
        super(source);
        this.option = option;
    }
    public Option getOption(){return option;}
}
