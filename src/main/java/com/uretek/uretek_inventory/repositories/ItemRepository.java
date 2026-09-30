package com.uretek.uretek_inventory.repositories;


import com.uretek.uretek_inventory.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {
}
