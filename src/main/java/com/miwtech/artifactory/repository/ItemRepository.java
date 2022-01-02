package com.miwtech.artifactory.repository;

import com.miwtech.artifactory.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    List<ItemEntity> findByQuantityGreaterThan(final Integer quantity);

}
