package com.shulgin.yandex_autumn.service;

import com.shulgin.yandex_autumn.dto.ImportElement;
import com.shulgin.yandex_autumn.dto.ImportRequest;
import com.shulgin.yandex_autumn.entity.Element;
import com.shulgin.yandex_autumn.entity.ElementType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
public class ValidationService {
    private final ElementService elementService;

    private final HashMap<UUID, ElementType> map;

    public ValidationService(ElementService elementService) {
        this.elementService = elementService;
        this.map  = new HashMap<>();
    }

    public boolean checkImportRequest(ImportRequest importRequest) {
        map.clear();
        ImportElement[] importElements = importRequest.getItems();
        for(ImportElement importElement : importElements) {
            if(map.containsKey(importElement.getId()) || importElement.getId() == null) {
                return false;
            }
            map.put(importElement.getId(), importElement.getType());
        }

        for(ImportElement importElement : importElements) {
            ElementType type = importElement.getType();
            switch (type) {
                case FOLDER:
                    if(!checkFolder(importElement)) {
                        return false;
                    }
                    break;
                case FILE:
                    if(!checkFile(importElement)) {
                        return false;
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    private boolean checkFolder(ImportElement importElement) {
        return importElement.getUrl() == null
                && importElement.getSize() == null
                && checkParent(importElement);
    }

    private boolean checkFile(ImportElement importElement) {
        Long size = importElement.getSize();
        String url = importElement.getUrl();
        if(size == null || size < 0) {
            return false;
        }

        if (url == null || url.length() >= 255) {
            return false;
        }
        return checkParent(importElement);
    }

    private boolean checkParent(ImportElement importElement) {
        UUID parentId = importElement.getParentId();
        if(parentId == null) {
            return true;
        }
        ElementType typeFromMap = map.get(parentId);
        if(typeFromMap == ElementType.FOLDER) {
            return true;
        }

        Optional<Element> optionalParent = elementService
                .findElementById(importElement.getParentId());
        if(!optionalParent.isPresent()) {
            return false;
        }

        Element parentElement = optionalParent.get();
        ElementType parentType = parentElement.getType();

        return parentType == ElementType.FOLDER;
    }
}
