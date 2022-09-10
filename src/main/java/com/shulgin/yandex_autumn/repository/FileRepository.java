package com.shulgin.yandex_autumn.repository;

import com.shulgin.yandex_autumn.entity.File;
import org.springframework.data.repository.CrudRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface FileRepository extends CrudRepository<File, UUID> {
    List<File> findFileByDateBetween(ZonedDateTime start, ZonedDateTime end);
}
