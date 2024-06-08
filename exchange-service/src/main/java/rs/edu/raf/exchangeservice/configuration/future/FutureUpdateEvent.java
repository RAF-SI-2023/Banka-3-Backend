package rs.edu.raf.exchangeservice.configuration.future;

import org.springframework.context.ApplicationEvent;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyFuture;

public class FutureUpdateEvent extends ApplicationEvent {
    private final MyFuture future;

    public FutureUpdateEvent(Object source, MyFuture future) {
        super(source);
        this.future = future;
    }
    public MyFuture getFuture(){return future;}
}
