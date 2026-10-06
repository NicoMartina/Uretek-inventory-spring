package com.uretek.uretek_inventory.controllers;

import com.uretek.uretek_inventory.entities.Item;
import com.uretek.uretek_inventory.services.ItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public Item create(@RequestBody Item item){
        return itemService.create(item);
    }

    @GetMapping
    public List<Item> getAllItems(){
        return itemService.getAllItems();
    }

    @PutMapping("/{id}")
    public Item update(@PathVariable UUID id, @RequestBody Item request){
        return itemService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id){
        itemService.delete(id);
    }


}
