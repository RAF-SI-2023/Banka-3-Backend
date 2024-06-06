package rs.edu.raf.exchangeservice.configuration.forex;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyForex;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ForexWebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("Connection established");
        this.sessions.add(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {

    }

    @EventListener
    public void sendForexUpdate(ForexUpdateEvent event) {
        MyForex forexUpdate = event.getForex();
        try {
            for (WebSocketSession session : this.sessions) {
                System.out.println(forexUpdate.toString() + "\n\n\n\n\n");
                session.sendMessage(new TextMessage(forexUpdate.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
