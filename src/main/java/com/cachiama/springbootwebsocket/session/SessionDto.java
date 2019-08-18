package com.cachiama.springbootwebsocket.session;

import lombok.Getter;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Getter
public class SessionDto {
    private final String principal;
    private final Set<String> subscriptions;
    private final Optional<String> name;

    private SessionDto(String principal, Set<String> subscriptions, Optional<String> name) {
        this.principal = principal;
        this.subscriptions = Collections.unmodifiableSet(subscriptions);
        this.name = name;
    }

    public static SessionDto initial(String principal) {
        return new SessionDto(principal, Collections.emptySet(), Optional.empty());
    }

    public SessionDto mapSubscriptions(Function<Set<String>, Set<String>> subscriptionMapper) {
        return new SessionDto(this.principal, subscriptionMapper.apply(this.subscriptions), this.name);
    }

    public SessionDto withName(String name) {
        return new SessionDto(this.principal, this.subscriptions, Optional.of(name));
    }

}