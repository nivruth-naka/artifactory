package com.miwtech.artifactory.service;

import com.miwtech.artifactory.model.Item;

import java.util.List;

public interface ItemService {

    List<Item> getItems();
    void buyItem(final Item item);

}
