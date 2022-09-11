package com.shulgin.yandex_autumn.repository;

import com.shulgin.yandex_autumn.entity.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {
}
