package com.uretek.uretek_inventory;


import com.uretek.uretek_inventory.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class JobControllerIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ItemRepository itemRepository;

    private String token;

    @BeforeEach
    void setup() throws Exception {
        // GET JWT Token
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"uretek123\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        token = response.replaceAll(".*\"token\":\"([^\"]+)\".*", "$1");

        // Seed Stock
        itemRepository.findAll().forEach(item -> {
            if (item.getName().equals("ISO Tank A")) {
                item.setCurrentStock(500.0);
                itemRepository.save(item);
            }
            if (item.getName().equals("Resina Tank B")) {
                item.setCurrentStock(500.0);
                itemRepository.save(item);
            }
        });
    }

    @Test
    void createJob_shouldReturn201() throws Exception {
        mockMvc.perform(post("/api/jobs")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                         "presupuestoNumber": "INT-001",
                         "jobDate": "2026-11-20",
                         "notes": "Integration test",
                         "mixTotal": 10.0,
                         "clientName": "Integration Client",
                         "status": "PENDIENTE",
                         "quotedAmount": 5000.0
                    }
                    """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.presupuestoNumber").value("INT-001"))
                .andExpect(jsonPath("$.status").value("PENDIENTE"));
    }

    @Test
    void getJobs_filterByStatus_shouldReturnOnlyMatching() throws Exception {
        mockMvc.perform(get("/api/jobs?status=PENDIENTE")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void createJob_insufficientStock_shouldReturn409() throws Exception {
        itemRepository.findAll().forEach(item -> {
            if (item.getName().equals("ISO Tank A")) {
                item.setCurrentStock(1.0);
                itemRepository.save(item);
            }
        });

        mockMvc.perform(post("/api/jobs")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "presupuestoNumber": "INT-002",
                            "jobDate": "2026-11-20",
                            "notes": "Should fail",
                            "mixTotal": 100.0,
                            "clientName": "Fail Client",
                            "status": "PENDIENTE",
                            "quotedAmount": 1000.0
                        }
                        """))
                .andExpect(status().isConflict());
    }
}
