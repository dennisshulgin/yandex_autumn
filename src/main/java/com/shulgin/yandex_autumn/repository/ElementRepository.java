package com.shulgin.yandex_autumn.repository;

import com.shulgin.yandex_autumn.entity.Element;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ElementRepository extends CrudRepository<Element, UUID> {
    Optional<Element> findElementById(UUID uuid);
}
