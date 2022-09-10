package com.shulgin.yandex_autumn.utils;

import com.shulgin.yandex_autumn.dto.ElementResponse;
import com.shulgin.yandex_autumn.entity.Element;
import com.shulgin.yandex_autumn.entity.File;
import com.shulgin.yandex_autumn.entity.Folder;

import java.util.List;

public class ResponseBuildUtil {
    public static ElementResponse buildResponse(Element element) {
        ElementResponse elementResponse = new ElementResponse();
        elementResponse.setDate(element.getDate());
        elementResponse.setType(element.getType());
        elementResponse.setParentId(element.getParentId());
        elementResponse.setSize(element.getSize());
        switch (element.getType()) {
            case FILE:
                File file = (File) element;
                elementResponse.setUrl(file.getUrl());
                elementResponse.setChildren(null);
                break;
            case FOLDER:
                Folder folder = (Folder) element;
                elementResponse.setUrl(null);
                List<Element> children = folder.getChildren();
                int childrenCount = children.size();
                ElementResponse[] childrenResponse = new ElementResponse[childrenCount];
                for (int i = 0; i < childrenCount; i++) {
                    childrenResponse[i] = buildResponse(children.get(i));
                }
                elementResponse.setChildren(childrenResponse);
                break;
            default:
                break;
        }
        return elementResponse;
    }
}
