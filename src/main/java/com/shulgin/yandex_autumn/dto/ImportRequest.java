package com.shulgin.yandex_autumn.dto;

import java.time.ZonedDateTime;

public class ImportRequest {
    private ImportElement[] items;
    private ZonedDateTime updateDate;

    public ImportRequest(ImportElement[] items, ZonedDateTime updateDate) {
        this.updateDate = updateDate;
        this.items = items;
    }

    public ZonedDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(ZonedDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public ImportElement[] getItems() {
        return items;
    }

    public void setItems(ImportElement[] items) {
        this.items = items;
    }
}
