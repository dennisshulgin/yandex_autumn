package com.shulgin.yandex_autumn.entity;

import javax.persistence.Entity;

@Entity
public class File extends Element {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
