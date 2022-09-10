package com.shulgin.yandex_autumn.utils;

import com.shulgin.yandex_autumn.dto.ImportElement;
import com.shulgin.yandex_autumn.entity.File;
import com.shulgin.yandex_autumn.entity.Folder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ConverterUtil {
    public static File convertElementToFile(ImportElement importElement) {
        File file = new File();
        file.setUrl(importElement.getUrl());
        file.setSize(importElement.getSize());
        file.setDate(importElement.getDate());
        file.setId(importElement.getId());
        file.setParentId(importElement.getParentId());
        file.setType(importElement.getType());
        return file;
    }

    public static Folder convertElementToFolder(ImportElement importElement) {
        Folder folder = new Folder();
        folder.setDate(importElement.getDate());
        folder.setId(importElement.getId());
        folder.setParentId(importElement.getParentId());
        folder.setType(importElement.getType());
        folder.setChildren(new ArrayList<>());
        return folder;
    }
}
