package com.uretek.uretek_inventory.controllers;

import com.uretek.uretek_inventory.dto.CreateJobRequest;
import com.uretek.uretek_inventory.dto.UpdateJobRequest;
import com.uretek.uretek_inventory.entities.Job;
import com.uretek.uretek_inventory.services.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<Job> create(@RequestBody CreateJobRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<Job>> getAllItems(){

        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> update(@PathVariable UUID id, @RequestBody UpdateJobRequest request){
        return ResponseEntity.ok(jobService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        jobService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
