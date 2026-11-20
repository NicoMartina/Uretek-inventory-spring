package com.uretek.uretek_inventory.repositories;

import com.uretek.uretek_inventory.entities.Job;
import com.uretek.uretek_inventory.entities.JobStatus;
import org.springframework.data.jpa.domain.Specification;

public class JobSpecification {

    public static Specification<Job> hasStatus(JobStatus status) {
        return ((root, query, criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("status"), status));
    }

    public static Specification<Job> hasClientName(String clientName) {
        return ((root, query, criteriaBuilder) ->
                clientName == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("clientName")), "%" + clientName.toLowerCase() + "%"));
    }
}
