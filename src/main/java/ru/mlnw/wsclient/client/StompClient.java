package ru.mlnw.wsclient.client;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.util.Scanner;

@Component
public class StompClient {

    private static final String URL = "ws://185.146.156.236:8888/ws/websocket";

    @PostConstruct
    public void init() {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("Authorization", "Basic " + DatatypeConverter.printBase64Binary("developer:developer".getBytes()));
        stompClient.connect(URL, handshakeHeaders, connectHeaders, sessionHandler);

        new Scanner(System.in).nextLine(); // Don't close immediately.
    }
}
