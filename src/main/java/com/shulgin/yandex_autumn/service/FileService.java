package com.shulgin.yandex_autumn.service;

import com.shulgin.yandex_autumn.entity.File;
import com.shulgin.yandex_autumn.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<File> findFileByDateBetween(ZonedDateTime start, ZonedDateTime end) {
        return fileRepository.findFileByDateBetween(start, end);
    }
}
