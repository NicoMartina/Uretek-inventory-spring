package com.uretek.uretek_inventory;

import com.uretek.uretek_inventory.dto.CreateJobRequest;
import com.uretek.uretek_inventory.entities.Item;
import com.uretek.uretek_inventory.entities.Job;
import com.uretek.uretek_inventory.repositories.ItemRepository;
import com.uretek.uretek_inventory.repositories.JobRepository;
import com.uretek.uretek_inventory.services.JobService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JobServiceTest {

    @Mock
    JobRepository jobRepository;

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    JobService jobService;

    private Item isoItem() {
        Item iso = new Item();
        iso.setName("ISO Tank A");
        iso.setCurrentStock(5000.0);
        return iso;
    }

    private Item resinaItem() {
        Item resina = new Item();
        resina.setName("Resina Tank B");
        resina.setCurrentStock(5000.0);
        return resina;
    }

    private CreateJobRequest basicRequest() {
        CreateJobRequest request = new CreateJobRequest();
        request.setPresupuestoNumber("UA001");
        request.setMixTotal(1000.0);
        request.setClientName("Client Test");
        return request;
    }

    @Test
    void shouldSaveJobWithCorrectData() {
        Job job = new Job();
        job.setPresupuestoNumber("UA001");
        job.setClientName("Client Test");

        when(itemRepository.findByName("ISO Tank A")).thenReturn(Optional.of(isoItem()));
        when(itemRepository.findByName("Resina Tank B")).thenReturn(Optional.of(resinaItem()));
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        Job result = jobService.create(basicRequest());

        assertEquals("UA001", result.getPresupuestoNumber());
        assertEquals("Client Test", result.getClientName());
        verify(jobRepository).save(any(Job.class));
    }

    @Test
    void shouldReduceISOStockWhenJobIsCreated() {
        when(itemRepository.findByName("ISO Tank A")).thenReturn(Optional.of(isoItem()));
        when(itemRepository.findByName("Resina Tank B")).thenReturn(Optional.of(resinaItem()));
        when(jobRepository.save(any(Job.class))).thenReturn(new Job());

        jobService.create(basicRequest());

        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
        verify(itemRepository, times(2)).save(captor.capture());

        List<Item> saved = captor.getAllValues();
        Item savedIso = saved.stream()
                .filter(i -> i.getName().equals("ISO Tank A"))
                .findFirst().orElseThrow();

        assertEquals(4370.0, savedIso.getCurrentStock()); // 5000 - (1000 * 0.63)
    }

    @Test
    void shouldReduceResinaWhenJobIsCreated() {
        when(itemRepository.findByName("ISO Tank A")).thenReturn(Optional.of(isoItem()));
        when(itemRepository.findByName("Resina Tank B")).thenReturn(Optional.of(resinaItem()));
        when(jobRepository.save(any(Job.class))).thenReturn(new Job());

        jobService.create(basicRequest());

        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
        verify(itemRepository, times(2)).save(captor.capture());

        List<Item> saved = captor.getAllValues();
        Item savedResina = saved.stream()
                .filter(i -> i.getName().equals("Resina Tank B"))
                .findFirst().orElseThrow();

        assertEquals(4630.0, savedResina.getCurrentStock()); // 5000 - (1000 * 0.37)
    }
}