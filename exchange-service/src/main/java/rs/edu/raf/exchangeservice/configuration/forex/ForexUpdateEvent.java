package rs.edu.raf.exchangeservice.configuration.forex;

import org.springframework.context.ApplicationEvent;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyForex;

public class ForexUpdateEvent extends ApplicationEvent {
    private final MyForex forex;

    public ForexUpdateEvent(Object source, MyForex forex) {
        super(source);
        this.forex = forex;
    }

    public MyForex getForex() {
        return forex;
    }
}
