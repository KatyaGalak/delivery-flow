package ru.ekaterina.gateway.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRegisteredEvent {
    @JsonProperty("user_id")
    public Long userId;

    @JsonProperty("username")
    public String username;

    @JsonProperty("name")
    public String name;

    @JsonProperty("email")
    public String email;

    @JsonProperty("time")
    public Long time;

    public UserRegisteredEvent(Long userId, String username, String name, String email) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.time = System.currentTimeMillis();
    }

    public UserRegisteredEvent() {}
}
