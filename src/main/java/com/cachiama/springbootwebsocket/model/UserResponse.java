package com.cachiama.springbootwebsocket.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserResponse {
    private boolean success;

    public UserResponse(boolean success) {
        this.success = success;
    }

}

