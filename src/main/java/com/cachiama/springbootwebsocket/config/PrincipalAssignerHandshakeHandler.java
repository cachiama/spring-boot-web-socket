package com.cachiama.springbootwebsocket.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Component
public class PrincipalAssignerHandshakeHandler extends DefaultHandshakeHandler {
    private static final String PRINCIPAL_KEY = PrincipalAssignerHandshakeHandler.class.getName() + ".principal";

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        return () -> (String) attributes.computeIfAbsent(PRINCIPAL_KEY, (value) -> generateRandomUsername());
    }


    private String generateRandomUsername() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}