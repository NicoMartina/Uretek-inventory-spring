package com.uretek.uretek_inventory.services;

import com.uretek.uretek_inventory.dto.CreateJobRequest;
import com.uretek.uretek_inventory.entities.Job;
import com.uretek.uretek_inventory.repositories.ItemRepository;
import com.uretek.uretek_inventory.repositories.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class JobService {
    private JobRepository jobRepository;
    private ItemRepository itemRepository;

    public JobService(JobRepository jobRepository, ItemRepository itemRepository) {
        this.jobRepository = jobRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public List<Job> getAllJobs(){
        return jobRepository.findAll();
    }

    @Transactional
    public Job create(CreateJobRequest request) {
        Job job = new Job();
        job.setPresupuestoNumber(request.getPresupuestoNumber());
        job.setJobDate(request.getJobDate());
        job.setNotes(request.getNotes());
        job.setMixTotal(request.getMixTotal());
        job.setClientName(request.getClientName());
        job.setStatus(request.getStatus());

        double isoAmount = request.getMixTotal() * 0.63;
        double resinaAmount = request.getMixTotal() * 0.37;

        itemRepository.findByName("ISO").ifPresent(iso -> {
            iso.setCurrentStock(iso.getCurrentStock() - isoAmount);
            itemRepository.save(iso);
        });

        itemRepository.findByName("Resina").ifPresent(resina -> {
            resina.setCurrentStock((resina.getCurrentStock() - resinaAmount));
            itemRepository.save(resina);
        });
        return jobRepository.save(job);
    }

    @Transactional
    public Job update(UUID id, Job details){
        return jobRepository.findById(id)
                .map(job ->{
                    job.setPresupuestoNumber(details.getPresupuestoNumber());
                    job.setJobDate(details.getJobDate());
                    job.setNotes(details.getNotes());
                    return jobRepository.save(job);
                })
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    @Transactional
    public void delete(UUID id) {
        jobRepository.deleteById(id);
    }

}
