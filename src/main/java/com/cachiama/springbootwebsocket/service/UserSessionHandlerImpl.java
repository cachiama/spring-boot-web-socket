package com.cachiama.springbootwebsocket.service;

import com.cachiama.springbootwebsocket.exception.NoSuchSessionException;
import com.cachiama.springbootwebsocket.session.PrincipalNamePair;
import com.cachiama.springbootwebsocket.session.SessionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserSessionHandlerImpl implements UserSessionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSessionHandlerImpl.class);

    private final Map<String, SessionDto> inMemoryStore = new ConcurrentHashMap<>();

    @Override
    public synchronized void registerSessionSubscription(String sessionId, String principal, String subscription, String subscriptionId) {
        inMemoryStore.compute(sessionId, (key, currentValue) ->
                Optional.ofNullable(currentValue).orElseGet(() -> SessionDto.initial(principal))
                        .mapSubscriptions(currentSubscriptions -> {
                            Map<String, String> result = new LinkedHashMap<>(currentSubscriptions);
                            result.put(subscriptionId, subscription);
                            LOGGER.debug(sessionId + " connected to " + subscription + ". Previous subscriptions were: " + currentSubscriptions);
                            return result;
                        }));
    }

    @Override
    public synchronized void deregisterSessionSubscription(String sessionId, String subscriptionId) {
        inMemoryStore.computeIfPresent(sessionId, (key, sessionDto) ->
                sessionDto.mapSubscriptions(currentSubscriptions -> {
                    Map<String, String> result = new LinkedHashMap<>(currentSubscriptions);
                    result.remove(subscriptionId);
                    LOGGER.debug(sessionId + " disconnected from " + subscriptionId + ". Remaining subscriptions: " + result);
                    return result;
                }));
    }

    @Override
    public synchronized void deregisterAllSessionSubscriptions(String sessionId) {
        SessionDto sessionDto = inMemoryStore.remove(sessionId);

        LOGGER.debug(Optional.ofNullable(sessionDto.getSubscriptionIdSubscriptionMap()).orElseGet(Collections::emptyMap)
                .keySet()
                .stream()
                .collect(Collectors.joining(",", "Session " + sessionId + " disconnected from ", "")));
    }

    @Override
    public boolean setName(String sessionId, String name) {
        inMemoryStore.compute(sessionId, (key, currentValue) ->
                Optional.ofNullable(currentValue).orElseThrow(() -> new NoSuchSessionException(sessionId))
                        .withName(name));

        return true;
    }

    @Override
    public Stream<PrincipalNamePair> getAllSessionUserNameDtos() {
        return this.inMemoryStore.values()
                .stream()
                .map(sessionDto -> new PrincipalNamePair(sessionDto.getPrincipal(), sessionDto.getName()));
    }
}
