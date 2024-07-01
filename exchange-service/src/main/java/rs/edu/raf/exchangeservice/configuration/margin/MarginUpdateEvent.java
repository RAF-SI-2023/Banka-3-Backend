package rs.edu.raf.exchangeservice.configuration.margin;

import org.springframework.context.ApplicationEvent;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyMarginStock;

import java.time.Clock;

public class MarginUpdateEvent extends ApplicationEvent {
    private final MyMarginStock marginStock;

    public MarginUpdateEvent(Object source, MyMarginStock marginStock) {
        super(source);
        this.marginStock = marginStock;
    }

    public MarginUpdateEvent(Object source, Clock clock, MyMarginStock marginStock) {
        super(source, clock);
        this.marginStock = marginStock;
    }

    public MyMarginStock getMarginStock() {
        return marginStock;
    }
}
