package com.shulgin.yandex_autumn.dto;

import com.shulgin.yandex_autumn.entity.ElementType;

import java.time.ZonedDateTime;
import java.util.UUID;

public class ImportElement {
    private UUID id;
    private ElementType type;
    private Long size;
    private String url;
    private UUID parentId;
    private ZonedDateTime date;

    public ImportElement(UUID id, ElementType type, Long size, String url, UUID parentId, ZonedDateTime date) {
        this.id = id;
        this.type = type;
        this.size = size;
        this.url = url;
        this.parentId = parentId;
        this.date = date;
    }

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

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
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

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ImportElement{" +
                "id=" + id +
                ", type=" + type +
                ", size=" + size +
                ", url='" + url  +
                ", parentId=" + parentId +
                ", date=" + date +
                '}';
    }
}
