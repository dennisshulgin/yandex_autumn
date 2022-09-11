package com.shulgin.yandex_autumn.dto;

public class StatisticResponse {
    private String updateDate;
    private long size;

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
