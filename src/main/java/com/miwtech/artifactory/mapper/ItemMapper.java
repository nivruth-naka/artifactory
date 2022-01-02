package com.miwtech.artifactory.mapper;

import com.miwtech.artifactory.entity.ItemEntity;
import com.miwtech.artifactory.model.Item;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    List<Item> itemEntitiesToItems(final List<ItemEntity> items);

}
