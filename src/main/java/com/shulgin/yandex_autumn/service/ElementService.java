package com.shulgin.yandex_autumn.service;

import com.shulgin.yandex_autumn.entity.Element;
import com.shulgin.yandex_autumn.entity.ElementType;
import com.shulgin.yandex_autumn.entity.File;
import com.shulgin.yandex_autumn.entity.Folder;
import com.shulgin.yandex_autumn.exception.ValidationException;
import com.shulgin.yandex_autumn.repository.ElementRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ElementService {
    private final ElementRepository elementRepository;

    public ElementService(ElementRepository elementRepository) {
        this.elementRepository = elementRepository;
    }

    public void saveFolder(Folder folder){
        save(folder);
    }

    public void saveFile(File file) {
        save(file);
        updateSize(file, file.getSize());
    }

    public void save(Element element) {
        elementRepository.save(element);
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
                parentElement.setSize(parentElement.getSize() + size);
                save(parentElement);
                updateSize(parentElement, size);
            }
        }
    }

    public void deleteById(UUID id) {
        elementRepository.deleteById(id);
    }
}
