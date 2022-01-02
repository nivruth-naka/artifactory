package com.miwtech.artifactory.service;

import com.miwtech.artifactory.entity.ItemEntity;
import com.miwtech.artifactory.mapper.ItemMapper;
import com.miwtech.artifactory.model.Item;
import com.miwtech.artifactory.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    // cache with item ID & queue of timestamps when item is viewed
    private final Map<Long, Queue<Long>> itemCache = new HashMap<>();


    @Override
    public List<Item> getItems() {
        return itemMapper.itemEntitiesToItems(itemRepository.findByQuantityGreaterThan(0));
    }

    @Override
    public void buyItem(final Item item) {
        final ItemEntity itemEntity = getItem(item);
        itemEntity.setQuantity(itemEntity.getQuantity() - 1);
        itemRepository.save(itemEntity);
        log.info("Sold item " + item + " , updated quantity : " + itemEntity.getQuantity());
    }


    /**
     * gets item from inventory, if it exists
     * (this is a private method but could be extended to an API endpoint)
     *
     * @param item
     * @return {@link ItemEntity}
     */
    private ItemEntity getItem(final Item item) {
        final Optional<ItemEntity> optionalItemEntity = itemRepository.findByQuantityGreaterThan(0).stream()
                .filter(ie -> ie.getName().equalsIgnoreCase(item.getName()))
                .filter(ie -> ie.getDescription().equalsIgnoreCase(item.getDescription()))
                .filter(ie -> ie.getPrice() == item.getPrice())
                .findFirst();
        if (optionalItemEntity.isPresent()) {
            final ItemEntity itemEntity = optionalItemEntity.get();
            update(itemEntity);
            return itemEntity;
        } else {
            throw new IllegalArgumentException("Item " + item + " is not available");
        }
    }

    /**
     * updates cache, sliding window & price as required
     *
     * @param itemEntity
     */
    private void update(final ItemEntity itemEntity) {
        final long currentTimeMillis = System.currentTimeMillis();
        Queue<Long> timeMillisQueue = itemCache.get(itemEntity.getId());
        if (timeMillisQueue != null) {
            timeMillisQueue.add(currentTimeMillis); //add current timestamp to cache
            // update sliding window (remove timestamps greater than an hour)
            while (currentTimeMillis - timeMillisQueue.peek() > 3600000) {
                timeMillisQueue.poll();
            }
            // update price if item was viewed more than 10 times within an hour
            if (timeMillisQueue.size() > 10) {
                itemEntity.setPrice((int) (itemEntity.getPrice() * 1.1));
                itemRepository.save(itemEntity);
                itemCache.remove(itemEntity.getId()); //invalidate cache
            }
        } else {
            // create new sliding window (item doesn't exist in cache yet)
            timeMillisQueue = new LinkedList<>();
            timeMillisQueue.add(currentTimeMillis);
            itemCache.put(itemEntity.getId(), timeMillisQueue);
        }
    }

}
