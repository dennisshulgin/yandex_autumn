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

@Service
public class ElementService {
    private final ElementRepository elementRepository;
    private final StatisticRepository statisticRepository;

    public ElementService(ElementRepository elementRepository,
                          StatisticRepository statisticRepository) {
        this.elementRepository = elementRepository;
        this.statisticRepository = statisticRepository;
    }

    public void saveFolder(Folder folder){
        save(folder);
    }

    public void saveFile(File file) {
        statistic(file, file.getSize());
        save(file);
        updateSize(file, file.getSize());
    }

    public void save(Element element) {
        Optional<Element> optionalElement = elementRepository.findElementById(element.getId());
        if(optionalElement.isPresent()) {
            Element oldElement = optionalElement.get();
            UUID oldParentId = oldElement.getParentId();
            UUID parentId = element.getParentId();
            long oldSize = oldElement.getSize();
            if(parentId != null && !parentId.equals(oldParentId)) {
                Optional<Element> oldParentOptional = elementRepository.findElementById(oldParentId);
                if(oldParentOptional.isPresent()) {
                    updateSize(oldElement, -oldSize);
                }
            }
        }
        elementRepository.save(element);
        updateDate(element);
    }

    public Optional<Element> findElementById(UUID id) {
        return elementRepository.findElementById(id);
    }


    public void updateSize(Element element, long size) {
        UUID parent = element.getParentId();
        if(parent != null) {
            Optional<Element> optionalParentElement = elementRepository.findElementById(parent);
            if(optionalParentElement.isPresent())
            {
                Element parentElement = optionalParentElement.get();
                statistic(parentElement, parentElement.getSize() + size);
                parentElement.setSize(parentElement.getSize() + size);
                save(parentElement);
                updateSize(parentElement, size);
            }
        }
    }

    public void updateDate(Element element) {
        UUID parent = element.getParentId();
        if(parent != null) {
            Optional<Element> optionalParentElement = elementRepository.findElementById(parent);
            if(optionalParentElement.isPresent())
            {
                Element parentElement = optionalParentElement.get();
                parentElement.setDate(element.getDate());
                save(parentElement);
                updateDate(parentElement);
            }
        }
    }

    public void deleteById(UUID id) {
        Optional<Element> optionalElement = elementRepository.findElementById(id);
        if(optionalElement.isPresent()) {
            Element element = optionalElement.get();
            long size = element.getSize();
            updateSize(element, -size);
            elementRepository.deleteById(id);
        }
    }

    public void statistic(Element element, long size) {
        UUID id = element.getId();
        Optional<Element> oldOptionalElement = elementRepository.findElementById(id);
        long oldSize = -1;
        if(oldOptionalElement.isPresent()) {
            Element oldElement = oldOptionalElement.get();
            oldSize = oldElement.getSize();
        }
        if(oldSize != size) {
            Statistic statistic = new Statistic();
            statistic.setDate(element.getDate());
            statistic.setSize(size);
            statistic.setElemId(element.getId());
            statisticRepository.save(statistic);
        }
    }
}
