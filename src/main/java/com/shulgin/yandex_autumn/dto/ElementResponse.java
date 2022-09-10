package com.shulgin.yandex_autumn.dto;

import com.shulgin.yandex_autumn.entity.ElementType;

import java.time.ZonedDateTime;
import java.util.UUID;

public class ElementResponse {
    private UUID id;
    private ElementType type;
    private long size;
    private String url;
    private UUID parentId;
    private String date;
    private ElementResponse[] children;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ElementType getType() {
        return type;
    }

    public void setType(ElementType type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ElementResponse[] getChildren() {
        return children;
    }

    public void setChildren(ElementResponse[] children) {
        this.children = children;
    }
}