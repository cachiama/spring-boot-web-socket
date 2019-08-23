package com.cachiama.springbootwebsocket.resource;

import com.cachiama.springbootwebsocket.config.WebSocketConfig;
import com.cachiama.springbootwebsocket.dto.UserDto;
import com.cachiama.springbootwebsocket.model.UserResponse;
import com.cachiama.springbootwebsocket.service.UserSessionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {


    private final UserSessionHandler userSessionHandler;

    public UserController(UserSessionHandler userSessionHandler) {
        this.userSessionHandler = userSessionHandler;
    }

    @MessageMapping(WebSocketConfig.USER_NAME)
    @SendToUser(WebSocketConfig.USER_NAME)
    public UserResponse getUser(SimpMessageHeaderAccessor headerAccessor, UserDto user) {
        return new UserResponse(userSessionHandler.setName(headerAccessor.getSessionId(), user.getName()));
    }
}
