package ru.mlnw.wsclient.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import ru.mlnw.wsclient.model.ChatMessage;

import java.lang.reflect.Type;

@Slf4j
public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("New session established : " + session.getSessionId());
        session.subscribe("/topic/chat/0c94683d-d4c0-4e29-9ba3-fbdde94c53e7", this);
        log.info("Subscribed to /topic/messages");
        session.send("/app/chat/4b112300-16d0-4320-910c-df73a05736f6", getSampleMessage());
        log.info("Message sent to websocket server");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ChatMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        ChatMessage msg = (ChatMessage) payload;
        log.info("Received : " + msg.getContent() + " from : " + msg.getSender());
    }

    /**
     * A sample message instance.
     * @return instance of <code>Message</code>
     */
    private ChatMessage getSampleMessage() {
        return ChatMessage.builder().content("test_message").sender("developer").build();
    }
}
