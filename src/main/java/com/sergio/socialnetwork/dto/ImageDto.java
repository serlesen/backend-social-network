package com.sergio.socialnetwork.dto;

public class ImageDto {

    private Long id;
    private String title;
    private byte[] content;

    public ImageDto() {
        super();
    }

    public ImageDto(Long id, String title, byte[] content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
