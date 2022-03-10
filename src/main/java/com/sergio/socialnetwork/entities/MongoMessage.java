package com.sergio.socialnetwork.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "app_messages")
public class MongoMessage {

    private String id;
    private String content;
    private MongoUser user;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdDate;

    public MongoMessage() {
        super();
    }

    public MongoMessage(String content, MongoUser user, LocalDateTime createdDate) {
        this.content = content;
        this.user = user;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public MongoUser getUser() {
        return user;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUser(MongoUser user) {
        this.user = user;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
