package ru.mlnw.wsclient.client;

import javax.websocket.*;
import javax.xml.bind.DatatypeConverter;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * ChatServer Client
 *
 * @author Jiji_Sasidharan
 */
@ClientEndpoint
public class WebsocketClientEndpoint extends Endpoint {

    Session userSession = null;
    private MessageHandler messageHandler;

    public WebsocketClientEndpoint(URI endpointURI) {
        try {
            ClientEndpointConfig.Builder configBuilder = ClientEndpointConfig.Builder.create();
            configBuilder.configurator(new ClientEndpointConfig.Configurator() {
                @Override
                public void beforeRequest(Map<String, List<String>> headers) {
                    headers.put("Authorization", Collections.singletonList("Basic " + DatatypeConverter.printBase64Binary("developer:developer".getBytes())));
                }
            });
            ClientEndpointConfig clientConfig = configBuilder.build();
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, clientConfig,  endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Callback hook for Connection open events.
     *
     * @param userSession the userSession which is opened.
     */

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        System.out.println("opening websocket");
        this.userSession = session;
    }

    /**
     * Callback hook for Connection close events.
     *
     * @param userSession the userSession which is getting closed.
     * @param reason the reason for connection close
     */
    /*@OnClose*/
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("closing websocket");
        this.userSession = null;
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }

    /**
     * register message handler
     *
     * @param msgHandler
     */
    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    /**
     * Send a message.
     *
     * @param message
     */
    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }

    /**
     * Message handler.
     *
     * @author Jiji_Sasidharan
     */
    public static interface MessageHandler {

        public void handleMessage(String message);
    }
}
