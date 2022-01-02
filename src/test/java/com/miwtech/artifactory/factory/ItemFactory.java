package com.miwtech.artifactory.factory;

import com.miwtech.artifactory.entity.ItemEntity;
import com.miwtech.artifactory.model.Item;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ItemFactory {

    public Item getItem() {
        final Item item = new Item();
        item.setName("wallet");
        item.setDescription("trifold");
        item.setPrice(44);
        return item;
    }

    public ItemEntity getItemEntity() {
        final ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(1L);
        itemEntity.setName("wallet");
        itemEntity.setDescription("trifold");
        itemEntity.setPrice(44);
        itemEntity.setQuantity(4);
        return itemEntity;
    }

}
