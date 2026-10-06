package com.uretek.uretek_inventory.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "presupuesto_number")
    private String presupuestoNumber;
    @Column(name = "job_date")
    private LocalDate jobDate;
    @Column(name = "notes")
    private String notes;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "mix_total")
    private Double mixTotal;

}
