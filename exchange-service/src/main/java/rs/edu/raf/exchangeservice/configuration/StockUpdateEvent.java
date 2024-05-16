package rs.edu.raf.exchangeservice.configuration;

import org.springframework.context.ApplicationEvent;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyStock;

public class StockUpdateEvent extends ApplicationEvent {
    private final MyStock stock;

    public StockUpdateEvent(Object source, MyStock stock) {
        super(source);
        this.stock = stock;
    }

    public MyStock getStock() {
        return stock;
    }
}

