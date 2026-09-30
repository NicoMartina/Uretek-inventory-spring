package com.uretek.uretek_inventory.services;

import com.uretek.uretek_inventory.entities.Item;
import com.uretek.uretek_inventory.repositories.ItemRepository;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ItemService {
    private ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @Transactional
    public List<Item> getAllItems(){
        return itemRepository.findAll();
    }

    @Transactional
    public Item create(Item item) { return itemRepository.save(item);}

    @Transactional
    public Item update(UUID id, Item details){
        return itemRepository.findById(id)
                .map( item -> {
                    item.setName(details.getName());
                    item.setCategory(details.getCategory());
                    item.setUnit(details.getUnit());
                    item.setMinimumStock(details.getMinimumStock());
                    item.setCurrentStock(details.getCurrentStock());
                    return itemRepository.save(item);
                })
                .orElseThrow(() -> new RuntimeException("Item Not Found"));

    }

    @Transactional
    public void delete(UUID id) {
        itemRepository.deleteById(id);
    }
}
