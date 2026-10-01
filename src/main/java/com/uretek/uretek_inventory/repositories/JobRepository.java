package com.uretek.uretek_inventory.repositories;

import com.uretek.uretek_inventory.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {
}
