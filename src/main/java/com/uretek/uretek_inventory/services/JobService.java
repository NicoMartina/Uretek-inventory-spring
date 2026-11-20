package com.uretek.uretek_inventory.services;

import com.uretek.uretek_inventory.dto.CreateJobRequest;
import com.uretek.uretek_inventory.dto.UpdateJobRequest;
import com.uretek.uretek_inventory.entities.Job;
import com.uretek.uretek_inventory.entities.JobStatus;
import com.uretek.uretek_inventory.exceptions.InsufficientStockException;
import com.uretek.uretek_inventory.exceptions.ResourceNotFoundException;
import com.uretek.uretek_inventory.repositories.ItemRepository;
import com.uretek.uretek_inventory.repositories.JobRepository;
import com.uretek.uretek_inventory.repositories.JobSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class JobService {
    private JobRepository jobRepository;
    private ItemRepository itemRepository;

    public JobService(JobRepository jobRepository, ItemRepository itemRepository) {
        this.jobRepository = jobRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional(readOnly = true)
    public Page<Job> getAllJobs(Pageable pageable, JobStatus jobStatus, String clientName){
        Specification<Job> spec = Specification
                .where(JobSpecification.hasStatus(jobStatus))
                .and(JobSpecification.hasClientName(clientName));
        return jobRepository.findAll(spec, pageable);
    }

    @Transactional
    public Job create(CreateJobRequest request) {
        var iso = itemRepository.findByName("ISO Tank A")
                .orElseThrow(() -> new ResourceNotFoundException("Item", "ISO"));

        var resina = itemRepository.findByName("Resina Tank B")
                .orElseThrow(() -> new ResourceNotFoundException("Item", "Resina"));

        double isoAmount = request.getMixTotal() * 0.63;
        double resinaAmount = request.getMixTotal() * 0.37;

        if (iso.getCurrentStock() < isoAmount) {
            throw new InsufficientStockException("ISO", iso.getCurrentStock(), isoAmount);
        }

        if ( resina.getCurrentStock() < resinaAmount){
            throw new InsufficientStockException("Resina", resina.getCurrentStock(), resinaAmount);
        }

        iso.setCurrentStock(iso.getCurrentStock() - isoAmount);
        resina.setCurrentStock(resina.getCurrentStock() - resinaAmount);
        itemRepository.save(iso);
        itemRepository.save(resina);

        Job job = new Job();
        job.setPresupuestoNumber(request.getPresupuestoNumber());
        job.setJobDate(request.getJobDate());
        job.setNotes(request.getNotes());
        job.setMixTotal(request.getMixTotal());
        job.setClientName(request.getClientName());
        job.setStatus(request.getStatus());
        job.setQuotedAmount(request.getQuotedAmount());

        return jobRepository.save(job);
    }


    @Transactional
    public Job update(UUID id, UpdateJobRequest request){
        Job job =  jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job", id));


        job.setPresupuestoNumber(request.getPresupuestoNumber());
        job.setJobDate(request.getJobDate());
        job.setNotes(request.getNotes());
        job.setMixTotal(request.getMixTotal());
        job.setClientName(request.getClientName());
        job.setStatus(request.getStatus());
        job.setQuotedAmount(request.getQuotedAmount());

        return jobRepository.save(job);

    }


    @Transactional
    public void delete(UUID id) {
        if (!jobRepository.existsById(id)) {
            throw new ResourceNotFoundException("Job", id);
        }
        jobRepository.deleteById(id);
    }

}
