package com.uretek.uretek_inventory.controllers;

import com.uretek.uretek_inventory.dto.CreateJobRequest;
import com.uretek.uretek_inventory.entities.Job;
import com.uretek.uretek_inventory.services.JobService;
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
    public ResponseEntity<Job> create(@RequestBody CreateJobRequest job){
        Job created = jobService.create(job);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public List<Job> getAllItems(){
        return jobService.getAllJobs();
    }

    @PutMapping("/{id}")
    public Job update(@PathVariable UUID id, @RequestBody Job request){
        Job updatedJob = jobService.update(id, request);
        return updatedJob;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id){
        jobService.delete(id);
    }
}
