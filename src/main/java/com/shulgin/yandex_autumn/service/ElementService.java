package com.shulgin.yandex_autumn.service;

import com.shulgin.yandex_autumn.entity.Element;
import com.shulgin.yandex_autumn.entity.File;
import com.shulgin.yandex_autumn.entity.Folder;
import com.shulgin.yandex_autumn.entity.Statistic;
import com.shulgin.yandex_autumn.repository.ElementRepository;
import com.shulgin.yandex_autumn.repository.StatisticRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Сервис работы с элементами базы данных.
 * Элемнты могут быть двух типов FOLDER и FILE.
 */
@Service
public class ElementService {
    private final ElementRepository elementRepository;
    private final StatisticRepository statisticRepository;

    public ElementService(ElementRepository elementRepository,
                          StatisticRepository statisticRepository) {
        this.elementRepository = elementRepository;
        this.statisticRepository = statisticRepository;
    }

    /**
     * Метод сохранения папки.
     * @param folder объект папки.
     */
    public void saveFolder(Folder folder){
        save(folder);
    }

    /**
     * Метод сохраниения файла.
     * @param file объект файла.
     */
    public void saveFile(File file) {
        statistic(file, file.getSize());
        save(file);
        updateSize(file, file.getSize());
    }

    /**
     * Метод сохранияет элемент файла или папки в БД.
     * @param element file или folder элемент.
     */
    public void save(Element element) {
        // проверка наличия элемнта в базе данных
        Optional<Element> optionalElement = elementRepository.findElementById(element.getId());
        // если элемент присутствует, то получаем его старый размер
        if(optionalElement.isPresent()) {
            Element oldElement = optionalElement.get();
            UUID oldParentId = oldElement.getParentId();
            UUID parentId = element.getParentId();
            long oldSize = oldElement.getSize();
            /*
            * Если элемент сменил родителя, то вычитаем размер файла
            * у предыдущего родителя и у всех вышестоящих папок рекурсивно.
            * */
            if(parentId != null && !parentId.equals(oldParentId)) {
                Optional<Element> oldParentOptional = elementRepository.findElementById(oldParentId);
                if(oldParentOptional.isPresent()) {
                    updateSize(oldElement, -oldSize);
                }
            }
        }
        elementRepository.save(element);
        // рекурсивно обновляем дату всех родителей элемента
        updateDate(element);
    }

    /**
     * Метод поиска элемента по идентификатору.
     * @param id идентификатор объекта.
     * @return Optional с элементом.
     */
    public Optional<Element> findElementById(UUID id) {
        return elementRepository.findElementById(id);
    }

    /**
     * Метод обновления размера
     * @param element элемент.
     * @param size размер элемента.
     */
    public void updateSize(Element element, long size) {
        //получение идентификатора родителя элемента
        UUID parent = element.getParentId();
        if(parent != null) {
            Optional<Element> optionalParentElement = elementRepository.findElementById(parent);
            if(optionalParentElement.isPresent())
            {
                Element parentElement = optionalParentElement.get();
                // сохранение статистики об изменении размера
                statistic(parentElement, parentElement.getSize() + size);
                // обновление у родителя размера содержимого
                parentElement.setSize(parentElement.getSize() + size);
                save(parentElement);
                // рекурсивно вызываем для вышестоящих элементов
                updateSize(parentElement, size);
            }
        }
    }

    /**
     * Метод обновления даты.
     * @param element элемент.
     */
    public void updateDate(Element element) {
        UUID parent = element.getParentId();
        if(parent != null) {
            Optional<Element> optionalParentElement = elementRepository.findElementById(parent);
            if(optionalParentElement.isPresent())
            {
                /*
                * Получаем элемент и рекурсивно обновляем дату
                * у всех вышестоящих элементов.
                */
                Element parentElement = optionalParentElement.get();
                parentElement.setDate(element.getDate());
                save(parentElement);
                updateDate(parentElement);
            }
        }
    }

    /**
     * Метод удаления элемента.
     * @param id идентификатор объекта.
     */
    public void deleteById(UUID id) {
        Optional<Element> optionalElement = elementRepository.findElementById(id);
        if(optionalElement.isPresent()) {
            /*
                Обновляем размер вышестоящих папок и
                удаляем элемент.
             */
            Element element = optionalElement.get();
            long size = element.getSize();
            updateSize(element, -size);
            elementRepository.deleteById(id);
        }
    }

    /**
     * Метод сохраниения статистики при изменении размера файла или папки.
     * @param element элемент.
     * @param size размер элемента.
     */
    public void statistic(Element element, long size) {
        UUID id = element.getId();
        Optional<Element> oldOptionalElement = elementRepository.findElementById(id);
        /*
            Получение старого размера элемента,
            в случае отсутствия -1.
         */
        long oldSize = -1;
        if(oldOptionalElement.isPresent()) {
            Element oldElement = oldOptionalElement.get();
            oldSize = oldElement.getSize();
        }
        // обновление информации при несовпадении размера.
        if(oldSize != size) {
            Statistic statistic = new Statistic();
            statistic.setDate(element.getDate());
            statistic.setSize(size);
            statistic.setElemId(element.getId());
            statisticRepository.save(statistic);
        }
    }
}
