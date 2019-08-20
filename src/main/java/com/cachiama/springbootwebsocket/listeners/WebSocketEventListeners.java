package com.cachiama.springbootwebsocket.listeners;

import com.cachiama.springbootwebsocket.service.UserSessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
public class WebSocketEventListeners {

    private final UserSessionHandler userSessionHandler;

    @Autowired
    public WebSocketEventListeners(UserSessionHandler userSessionHandler) {
        this.userSessionHandler = userSessionHandler;
    }

    @EventListener
    public void handleSubscribe(final SessionSubscribeEvent event) {
        final SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        userSessionHandler.registerSessionSubscription(headers.getSessionId(), headers.getUser().getName(), headers.getDestination(), headers.getSubscriptionId());
    }

    @EventListener
    public void handleUnsubscribe(final SessionUnsubscribeEvent event) {
        final SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        userSessionHandler.deregisterSessionSubscription(headers.getSessionId(), headers.getSubscriptionId());
    }


    @EventListener
    public void handleDisconnect(final SessionDisconnectEvent event) {
        final SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        userSessionHandler.deregisterAllSessionSubscriptions(headers.getSessionId());
    }
}
