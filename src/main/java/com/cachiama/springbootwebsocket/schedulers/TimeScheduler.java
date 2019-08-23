package com.cachiama.springbootwebsocket.schedulers;

import com.cachiama.springbootwebsocket.config.WebSocketConfig;
import com.cachiama.springbootwebsocket.model.TimeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class TimeScheduler {

    private final SimpMessagingTemplate template;

    @Autowired
    public TimeScheduler(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Scheduled(fixedDelay = 1_000)
    public void broadcastToTime() {
        template.convertAndSend(WebSocketConfig.TOPIC + WebSocketConfig.TIME_TOPIC, new TimeResponse());
    }
}
