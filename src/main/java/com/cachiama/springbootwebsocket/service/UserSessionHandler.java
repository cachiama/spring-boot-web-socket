package com.cachiama.springbootwebsocket.service;

import com.cachiama.springbootwebsocket.session.PrincipalNamePair;

import java.util.stream.Stream;

public interface UserSessionHandler {
    void registerSessionSubscription(String sessionId, String principal, String subscription, String subscriptionId);

    void deregisterSessionSubscription(String sessionId, String subscriptionId);

    void deregisterAllSessionSubscriptions(String sessionId);

    boolean setName(String sessionId, String name);

    Stream<PrincipalNamePair> getAllSessionUserNameDtos();
}
