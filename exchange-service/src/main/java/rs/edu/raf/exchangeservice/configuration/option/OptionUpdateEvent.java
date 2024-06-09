package rs.edu.raf.exchangeservice.configuration.option;

import org.springframework.context.ApplicationEvent;
import rs.edu.raf.exchangeservice.domain.model.listing.Option;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyOption;

public class OptionUpdateEvent extends ApplicationEvent {

    private final MyOption option;

    public OptionUpdateEvent(Object source, MyOption option) {
        super(source);
        this.option = option;
    }
    public MyOption getOption(){return option;}
}
