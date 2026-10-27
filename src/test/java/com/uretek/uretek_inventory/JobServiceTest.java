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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class JobServiceTest {

    @Mock
    JobRepository jobRepository;

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    JobService jobService;

    @Test
    void shouldSaveJoveWithCorrectData(){
        // ARRANGE
        CreateJobRequest request = new CreateJobRequest();
        request.setPresupuestoNumber("UA001");
        request.setMixTotal(10000.0);
        request.setClientName("Client Test");

        Job job = new Job();
        job.setPresupuestoNumber("UA001");
        job.setClientName("Client Test");

        when(jobRepository.save(any())).thenReturn(job);

        Job result = jobService.create(request);

        assertEquals("UA001", result.getPresupuestoNumber());
        assertEquals("Client Test", result.getClientName());
        verify(jobRepository).save(any());
    }

    @Test
    void shouldReduceISOStockWhenJobIsCreated(){
        // ARRANGE
        Item isoItem = new Item();
        isoItem.setName("ISO");
        isoItem.setCurrentStock(5000.00);

        Job job = new Job();
        job.setPresupuestoNumber("UA001");
        job.setClientName("Nick");

        CreateJobRequest request = new CreateJobRequest();
        request.setPresupuestoNumber("UA001");
        request.setClientName("Nick");
        request.setMixTotal(1000.00);


        when(itemRepository.findByName("ISO")).thenReturn(Optional.of(isoItem));
        when(jobRepository.save(any())).thenReturn(job);


        // ACT
        jobService.create(request);


        // ASSERT
        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
        verify(itemRepository).save(captor.capture());
        assertEquals(4370.0, captor.getValue().getCurrentStock());
    }

    @Test
    void shouldReduceResinaWhenJobIsCreated(){
        // ARRANGE
        Item item = new Item();
        item.setName("Resina");
        item.setCurrentStock(5000.00);

        Job job = new Job();
        job.setPresupuestoNumber("UA001");
        job.setClientName("Nick");


        CreateJobRequest request = new CreateJobRequest();
        request.setPresupuestoNumber("UA001");
        request.setClientName("Nick");
        request.setMixTotal(1000.00);

        when(itemRepository.findByName("ISO")).thenReturn(Optional.empty());
        when(itemRepository.findByName("Resina")).thenReturn(Optional.of(item));
        when(jobRepository.save(any())).thenReturn(job);

        // ACT
        jobService.create(request);

        // ASSERT
        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
        verify(itemRepository).save(captor.capture());
        assertEquals(4630.00, captor.getValue().getCurrentStock());
    }


}
