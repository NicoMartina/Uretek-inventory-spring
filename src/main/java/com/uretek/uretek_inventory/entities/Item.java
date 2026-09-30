package com.uretek.uretek_inventory.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue( strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String category;
    private String unit;
    private Double currentStock;
    private Double minimumStock;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
