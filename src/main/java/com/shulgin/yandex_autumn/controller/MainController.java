package com.shulgin.yandex_autumn.controller;

import com.shulgin.yandex_autumn.dto.ElementResponse;
import com.shulgin.yandex_autumn.dto.ImportElement;
import com.shulgin.yandex_autumn.dto.ImportRequest;
import com.shulgin.yandex_autumn.dto.StatisticResponse;
import com.shulgin.yandex_autumn.entity.*;
import com.shulgin.yandex_autumn.exception.ItemNotFoundException;
import com.shulgin.yandex_autumn.exception.ValidationException;
import com.shulgin.yandex_autumn.service.ElementService;
import com.shulgin.yandex_autumn.service.FileService;
import com.shulgin.yandex_autumn.service.ValidationService;
import com.shulgin.yandex_autumn.utils.ConverterUtil;
import com.shulgin.yandex_autumn.utils.ResponseBuildUtil;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Основной контроллер, принимающий запросы от клиентов.
 */
@RestController
public class MainController {

    private final ValidationService validationService;
    private final ElementService elementService;
    private final FileService fileService;

    public MainController(ValidationService validationService,
                          ElementService elementService,
                          FileService fileService) {
        this.validationService = validationService;
        this.elementService = elementService;
        this.fileService = fileService;
    }

    /**
     * Метод импортирует новые файлы и папки или обновляет информацию об уже существующих.
     * @param importRequest объект запроса. Содержит дату и элементы из запроса.
     * @throws ValidationException исключение во время валидации запроса.
     */
    @PostMapping(value = "/imports")
    public void imports(@RequestBody ImportRequest importRequest) throws ValidationException {
        // проверка запроса на корректность
        boolean isValidRequest = validationService.checkImportRequest(importRequest);
        ZonedDateTime updateDate = importRequest.getUpdateDate();
        if(!isValidRequest) {
            throw new ValidationException();
        }

        ImportElement[] importElements = importRequest.getItems();
        Arrays.sort(importElements, (o1, o2) -> {
            if(o1.getType() == ElementType.FOLDER && o2.getType() == ElementType.FOLDER) {
                return 0;
            } else if(o1.getType() == ElementType.FOLDER && o2.getType() != ElementType.FOLDER) {
                return -1;
            } else {
                return 1;
            }
        });
        // конвертация элементов в объекты для базы данных
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

    /**
     * Метод удаляет элемент по ID.
     * @param id идентификатор объекта.
     * @throws ItemNotFoundException исключение в случае отсутствия элемента в БД.
     */
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable UUID id) throws ItemNotFoundException {
        Optional<Element> optionalElement = elementService.findElementById(id);
        if(!optionalElement.isPresent()) {
            throw new ItemNotFoundException();
        }
        elementService.deleteById(id);
    }

    /**
     * Метод проверяет наличие элемента в БД и возвращает дерево элементов, начиная с текущего.
     * @param id идентификатор объекта.
     * @return дерево элементов.
     * @throws ItemNotFoundException исключение в случае отсутствия элемента в БД.
     */
    @GetMapping("/nodes/{id}")
    public ElementResponse nodes(@PathVariable UUID id) throws ItemNotFoundException {
        Optional<Element> optionalElement = elementService.findElementById(id);
        if(!optionalElement.isPresent()) {
            throw new ItemNotFoundException();
        }
        return ResponseBuildUtil.buildResponse(optionalElement.get());
    }

    /**
     * Метод возвращает элементы, которые были обновлены в последние 24 часа.
     * @param date дата запроса.
     * @return массив элементов, обновленных за последний 24 часа.
     * @throws ValidationException исключение при валидации даты.
     */
    @GetMapping("/updates")
    public ElementResponse[] updates(@RequestParam("date") String date) throws ValidationException{
        ZonedDateTime endDate;
        try {
            endDate = ResponseBuildUtil.stringToZonedDateTime(date);
        } catch (DateTimeParseException e) {
            throw new ValidationException();
        }
        ZonedDateTime startDate = endDate.minusHours(24);
        List<File> files = fileService.findFileByDateBetween(startDate, endDate);
        ElementResponse[] response = new ElementResponse[files.size()];
        for (int i = 0; i < files.size(); i++) {
            response[i] = ResponseBuildUtil.buildResponse(files.get(i));
        }
        return response;
    }

    /**
     * Метод возвращает статистику изменений размера файла или каталога по заданному идентификатору.
     * @param id идентификатор объекта.
     * @return массив с изменениями объекта.
     */
    @GetMapping("/node/{id}/history")
    public StatisticResponse[] history(@PathVariable UUID id) {
        Optional<Element> optionalElement = elementService.findElementById(id);
        if(optionalElement.isPresent()) {
            Element element = optionalElement.get();
            List<Statistic> statistics = element.getStatistics();
            int historySize = statistics.size();
            StatisticResponse[] statisticResponses = new StatisticResponse[historySize];
            for (int i = 0; i < historySize; i++) {
                statisticResponses[i] = new StatisticResponse();
                statisticResponses[i].setSize(statistics.get(i).getSize());
                statisticResponses[i].setUpdateDate(ResponseBuildUtil
                        .zonedDateTimeToString(statistics.get(i).getDate()));
            }
            return statisticResponses;
        }
        return null;
    }
}
