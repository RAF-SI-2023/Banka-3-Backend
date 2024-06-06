package rs.edu.raf.exchangeservice.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import rs.edu.raf.exchangeservice.configuration.contract.ContractWebSocketHandler;
import rs.edu.raf.exchangeservice.configuration.forex.ForexWebSocketHandler;
import rs.edu.raf.exchangeservice.configuration.future.FutureWebSockettHandler;
import rs.edu.raf.exchangeservice.configuration.option.OptionWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private StockWebSocketHandler stockWebSocketHandler;
    @Autowired
    private OptionWebSocketHandler optionWebSocketHandler;
    @Autowired
    private ForexWebSocketHandler forexWebSocketHandler;
    @Autowired
    private ContractWebSocketHandler contractWebSocketHandler;
    @Autowired
    private FutureWebSockettHandler futureWebSocketHandler;


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(stockWebSocketHandler, "/ws/stocks").setAllowedOrigins("*");
        registry.addHandler(futureWebSocketHandler, "/ws/futures").setAllowedOrigins("*");
        registry.addHandler(forexWebSocketHandler, "/ws/forex").setAllowedOrigins("*");
        registry.addHandler(contractWebSocketHandler, "/ws/contract").setAllowedOrigins("*");
        registry.addHandler(optionWebSocketHandler, "/ws/option").setAllowedOrigins("*");
    }
}
