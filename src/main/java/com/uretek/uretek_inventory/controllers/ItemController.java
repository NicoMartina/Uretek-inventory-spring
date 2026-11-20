package com.uretek.uretek_inventory.controllers;

import com.uretek.uretek_inventory.entities.Item;
import com.uretek.uretek_inventory.services.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // CREATE AN ITEM
    @PostMapping
    public Item create(@RequestBody Item item){
        return itemService.create(item);
    }

    // FETCH ALL ITEMS
    @GetMapping
    public Page<Item> getAllItems(@PageableDefault(size = 10, sort = "name")Pageable pageable){
        return itemService.getAllItems(pageable);
    }

    // UPDATE AN ITEM
    @PutMapping("/{id}")
    public Item update(@PathVariable UUID id, @RequestBody Item request){
        return itemService.update(id, request);
    }

    // DELETE AN ITEM
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id){
        itemService.delete(id);
    }


}
