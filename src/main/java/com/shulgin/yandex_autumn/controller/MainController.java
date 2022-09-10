package com.shulgin.yandex_autumn.controller;

import com.shulgin.yandex_autumn.dto.ElementResponse;
import com.shulgin.yandex_autumn.dto.ImportElement;
import com.shulgin.yandex_autumn.dto.ImportRequest;
import com.shulgin.yandex_autumn.entity.Element;
import com.shulgin.yandex_autumn.entity.ElementType;
import com.shulgin.yandex_autumn.entity.File;
import com.shulgin.yandex_autumn.entity.Folder;
import com.shulgin.yandex_autumn.exception.ItemNotFoundException;
import com.shulgin.yandex_autumn.exception.ValidationException;
import com.shulgin.yandex_autumn.service.ElementService;
import com.shulgin.yandex_autumn.service.ValidationService;
import com.shulgin.yandex_autumn.utils.ConverterUtil;
import com.shulgin.yandex_autumn.utils.ResponseBuildUtil;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
public class MainController {

    private final ValidationService validationService;
    private final ElementService elementService;

    public MainController(ValidationService validationService, ElementService elementService) {
        this.validationService = validationService;
        this.elementService = elementService;
    }

    @PostMapping(value = "/imports")
    public void imports(@RequestBody ImportRequest importRequest) throws ValidationException{
        boolean isValidRequest = validationService.checkImportRequest(importRequest);
        ZonedDateTime updateDate = importRequest.getUpdateDate();
        if(!isValidRequest) {
            throw new ValidationException();
        }

        ImportElement[] importElements = importRequest.getItems();

        for(ImportElement importElement : importElements) {
            ElementType elementType = importElement.getType();
            switch (elementType) {
                case FILE:
                    File file = ConverterUtil
                            .convertElementToFile(importElement);
                    file.setDate(updateDate);
                    elementService.saveFile(file);
                    break;
                case FOLDER:
                    Folder folder = ConverterUtil
                            .convertElementToFolder(importElement);
                    folder.setDate(updateDate);
                    elementService.saveFolder(folder);
                    break;
                default:
                    break;
            }
        }
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable UUID id) throws ItemNotFoundException {
        Optional<Element> optionalElement = elementService.findElementById(id);
        if(!optionalElement.isPresent()) {
            throw new ItemNotFoundException();
        }
        elementService.deleteById(id);
    }

    @GetMapping("/nodes/{id}")
    public ElementResponse nodes(@PathVariable UUID id) throws ItemNotFoundException{
        Optional<Element> optionalElement = elementService.findElementById(id);
        if(!optionalElement.isPresent()) {
            throw new ItemNotFoundException();
        }
        return ResponseBuildUtil.buildResponse(optionalElement.get());
    }
}
