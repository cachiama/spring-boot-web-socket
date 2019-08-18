package com.cachiama.springbootwebsocket.service;

import com.cachiama.springbootwebsocket.exception.NoSuchSessionException;
import com.cachiama.springbootwebsocket.session.PrincipalNamePair;
import com.cachiama.springbootwebsocket.session.SessionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashSet;
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
    public synchronized void registerSessionSubscription(String sessionId, String principal, String subscription) {
        inMemoryStore.compute(sessionId, (key, currentValue) ->
                Optional.ofNullable(currentValue).orElseGet(() -> SessionDto.initial(principal))
                        .mapSubscriptions(currentSubscriptions -> {
                            LinkedHashSet<String> result = new LinkedHashSet<>(currentSubscriptions);
                            result.add(subscription);
                            LOGGER.debug(sessionId + " connected to " + subscription + ". Previous subscriptions were: " + currentSubscriptions);
                            return result;
                        }));
    }

    @Override
    public synchronized void deregisterSessionSubscription(String sessionId, String subscription) {
        inMemoryStore.computeIfPresent(sessionId, (key, sessionDto) ->
                sessionDto.mapSubscriptions(currentSubscriptions -> {
                    LinkedHashSet<String> result = new LinkedHashSet<>(currentSubscriptions);
                    result.remove(subscription);
                    LOGGER.debug(sessionId + " disconnected from " + subscription + ". Remaining subscriptions: " + result);
                    return result;
                }));
    }

    @Override
    public synchronized void deregisterAllSessionSubscriptions(String sessionId) {
        SessionDto sessionDto = inMemoryStore.remove(sessionId);

        LOGGER.debug(Optional.ofNullable(sessionDto.getSubscriptions()).orElseGet(Collections::emptySet)
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
