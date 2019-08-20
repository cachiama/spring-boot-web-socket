package com.cachiama.springbootwebsocket.session;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Getter
public class SessionDto {
    private final String principal;
    private final Map<String, String> subscriptionIdSubscriptionMap;
    private final Optional<String> name;

    private SessionDto(String principal, Map<String, String> subscriptions, Optional<String> name) {
        this.principal = principal;
        this.subscriptionIdSubscriptionMap = Collections.unmodifiableMap(subscriptions);
        this.name = name;
    }

    public static SessionDto initial(String principal) {
        return new SessionDto(principal, Collections.emptyMap(), Optional.empty());
    }

    public SessionDto mapSubscriptions(Function<Map<String, String>, Map<String, String>> subscriptionMapper) {
        return new SessionDto(this.principal, subscriptionMapper.apply(this.subscriptionIdSubscriptionMap), this.name);
    }

    public SessionDto withName(String name) {
        return new SessionDto(this.principal, this.subscriptionIdSubscriptionMap, Optional.of(name));
    }

}