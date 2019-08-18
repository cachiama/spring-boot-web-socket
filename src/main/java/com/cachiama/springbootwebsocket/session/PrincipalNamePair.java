package com.cachiama.springbootwebsocket.session;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
@Getter
public class PrincipalNamePair {

    private final String principal;
    private final Optional<String> username;

}
