package com.example.demo.controller;

import com.example.demo.dto.ItemRequestDto;
import com.example.demo.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<Long> createItem(@RequestBody ItemRequestDto itemRequestDto) {
        Long itemId = itemService.createItem(itemRequestDto.getName(),
                itemRequestDto.getDescription(),
                itemRequestDto.getOwnerId(),
                itemRequestDto.getManagerId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(itemId);
    }
}
