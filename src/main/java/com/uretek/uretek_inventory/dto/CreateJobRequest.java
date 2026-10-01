package com.uretek.uretek_inventory.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CreateJobRequest {

    private String presupuestoNumber;
    private LocalDate jobDate;
    private String notes;
    private Double mixTotal;
}
