package com.shulgin.yandex_autumn.service;

import com.shulgin.yandex_autumn.dto.ImportElement;
import com.shulgin.yandex_autumn.dto.ImportRequest;
import com.shulgin.yandex_autumn.entity.Element;
import com.shulgin.yandex_autumn.entity.ElementType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис валидации входящего запроса.
 */
@Service
public class ValidationService {
    private final ElementService elementService;

    private final HashMap<UUID, ElementType> map;

    public ValidationService(ElementService elementService) {
        this.elementService = elementService;
        this.map  = new HashMap<>();
    }

    /**
     * метод проверки запроса.
     * @param importRequest запрос.
     * @return true или false.
     */
    public boolean checkImportRequest(ImportRequest importRequest) {
        map.clear();
        ImportElement[] importElements = importRequest.getItems();
        /*
            Добавление всех идентификаторов в HashMap
            с указанием ID и типа элемента(файл, папка).
         */
        for(ImportElement importElement : importElements) {
            if(map.containsKey(importElement.getId()) || importElement.getId() == null) {
                return false;
            }
            map.put(importElement.getId(), importElement.getType());
        }
        // проверка элементов по типу
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

    /**
     * Метод проверки папки на валидность.
     * @param importElement элемент типа FOLDER.
     * @return true или false.
     */
    private boolean checkFolder(ImportElement importElement) {
        /*
            По заданому условию:
            - не должно быть поля size
            - не должно быть поля url
         */
        return importElement.getUrl() == null
                && importElement.getSize() == null
                && checkParent(importElement);
    }

    /**
     * Метод проверки файла на валидность.
     * @param importElement элемент типа FILE.
     * @return true или false.
     */
    private boolean checkFile(ImportElement importElement) {
        Long size = importElement.getSize();
        String url = importElement.getUrl();
        // по условию полк size должно быть > 0
        if(size == null || size < 0) {
            return false;
        }

        // по условию длина url не должна превышать 255 символов
        if (url == null || url.length() >= 255) {
            return false;
        }
        return checkParent(importElement);
    }

    /**
     * Метод проверки родителя элемента.
     * @param importElement элемент.
     * @return true или false.
     */
    private boolean checkParent(ImportElement importElement) {
        // проверяем наличие идентификатора родителя, может быть null
        UUID parentId = importElement.getParentId();
        if(parentId == null) {
            return true;
        }
        // проверяем наличия родителя среди элементов в текущем запросе с типом папка.
        ElementType typeFromMap = map.get(parentId);
        if(typeFromMap == ElementType.FOLDER) {
            return true;
        }
        // если родитель не найден, про проверяем наличие родителя в базе данных
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
