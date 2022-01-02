package com.miwtech.artifactory.service;

import com.miwtech.artifactory.entity.ItemEntity;
import com.miwtech.artifactory.factory.ItemFactory;
import com.miwtech.artifactory.mapper.ItemMapper;
import com.miwtech.artifactory.model.Item;
import com.miwtech.artifactory.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private ItemMapper itemMapper;


    @Test
    void getItems() {
        final List<ItemEntity> itemEntities = List.of(ItemFactory.getItemEntity());
        final List<Item> items = List.of(ItemFactory.getItem());
        when(itemRepository.findByQuantityGreaterThan(anyInt())).thenReturn(itemEntities);
        when(itemMapper.itemEntitiesToItems(anyList())).thenReturn(items);
        assertEquals(items, itemService.getItems());
        verify(itemRepository).findByQuantityGreaterThan(0);
        verify(itemMapper).itemEntitiesToItems(itemEntities);
    }

    @Test
    void getItems_zeroQuantity() {
        final ItemEntity itemEntity = ItemFactory.getItemEntity();
        itemEntity.setQuantity(0);
        final List<ItemEntity> itemEntities = List.of(itemEntity);
        when(itemRepository.findByQuantityGreaterThan(anyInt())).thenReturn(itemEntities);
        assertEquals(Collections.EMPTY_LIST, itemService.getItems());
        verify(itemRepository).findByQuantityGreaterThan(0);
        verify(itemMapper).itemEntitiesToItems(itemEntities);
    }

    @Test
    void buyItem() {
        final ItemEntity itemEntity = ItemFactory.getItemEntity();
        final int quantity = itemEntity.getQuantity();
        final List<ItemEntity> itemEntities = List.of(itemEntity);
        when(itemRepository.findByQuantityGreaterThan(anyInt())).thenReturn(itemEntities);
        itemService.buyItem(ItemFactory.getItem());
        verify(itemRepository).findByQuantityGreaterThan(0);
        assertEquals(quantity - 1, itemEntity.getQuantity());
    }

    @Test
    void buyItem_notAvailable() {
        when(itemRepository.findByQuantityGreaterThan(anyInt())).thenReturn(List.of(ItemFactory.getItemEntity()));
        assertThrows(IllegalArgumentException.class, () -> itemService.buyItem(new Item()));
    }

    @Test
    void buyItem_priceChange() {
        final ItemEntity itemEntity = ItemFactory.getItemEntity();
        itemEntity.setQuantity(100);
        final int price = itemEntity.getPrice();
        final List<ItemEntity> itemEntities = List.of(itemEntity);
        when(itemRepository.findByQuantityGreaterThan(anyInt())).thenReturn(itemEntities);
        itemService.buyItem(ItemFactory.getItem());
        itemService.buyItem(ItemFactory.getItem());
        itemService.buyItem(ItemFactory.getItem());
        itemService.buyItem(ItemFactory.getItem());
        itemService.buyItem(ItemFactory.getItem());
        itemService.buyItem(ItemFactory.getItem());
        itemService.buyItem(ItemFactory.getItem());
        itemService.buyItem(ItemFactory.getItem());
        itemService.buyItem(ItemFactory.getItem());
        itemService.buyItem(ItemFactory.getItem());
        itemService.buyItem(ItemFactory.getItem());
        verify(itemRepository, times(11)).findByQuantityGreaterThan(0);
        assertEquals((int) (price * 1.1), itemEntity.getPrice());
    }

}
