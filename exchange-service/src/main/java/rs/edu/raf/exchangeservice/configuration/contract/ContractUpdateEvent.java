package rs.edu.raf.exchangeservice.configuration.contract;

import org.springframework.context.ApplicationEvent;
import rs.edu.raf.exchangeservice.domain.model.myListing.Contract;

public class ContractUpdateEvent extends ApplicationEvent {
    private final Contract contract;

    public ContractUpdateEvent(Object source, Contract contract) {
        super(source);
        this.contract = contract;
    }

    public Contract getContract(){return contract;}
}
