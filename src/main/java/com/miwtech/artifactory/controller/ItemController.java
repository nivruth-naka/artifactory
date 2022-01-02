package com.miwtech.artifactory.controller;

import com.miwtech.artifactory.model.Item;
import com.miwtech.artifactory.service.ItemService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;


    @GetMapping
    public ResponseEntity<List<Item>> getItems() {
        return ResponseEntity.ok(itemService.getItems());
    }

    @PostMapping
    public ResponseEntity<Void> buyItem(@RequestBody @NonNull final Item item) {
        itemService.buyItem(item);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
