package com.cachiama.springbootwebsocket.model;

import java.util.Date;

public class TimeResponse {
    private final Date time;
    private final String name;

    public TimeResponse() {
        this("anonymous");
    }

    public TimeResponse(String name) {
        this.time = new Date();
        this.name = name;
    }

    public String getMessage() {
        return "Greetings " + name + ". Current time: " + time.toString();
    }
}
