package com.cachiama.springbootwebsocket.schedulers;

import com.cachiama.springbootwebsocket.config.WebSocketConfig;
import com.cachiama.springbootwebsocket.model.TimeResponse;
import com.cachiama.springbootwebsocket.service.UserSessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class UserTimeScheduler {

    private final SimpMessagingTemplate template;

    private final UserSessionHandler userSessionHandler;

    @Autowired
    public UserTimeScheduler(SimpMessagingTemplate template, UserSessionHandler userSessionHandler) {
        this.template = template;
        this.userSessionHandler = userSessionHandler;
    }

    @Scheduled(fixedDelay = 1_000)
    public void broadcastToTime() {
        this.userSessionHandler.getAllSessionUserNameDtos()
                .filter(dto -> dto.getUsername().isPresent())
                .forEach(dto -> template.convertAndSendToUser(dto.getPrincipal(), WebSocketConfig.SUBSCRIBE_USER_REPLY, new TimeResponse(dto.getUsername().get())));
    }
}
