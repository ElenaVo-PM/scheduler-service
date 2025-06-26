package com.example.scheduler.domain.repository;

import com.example.scheduler.domain.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends CrudRepository<Event, UUID> {

    List<Event> findAllByOwnerId(UUID ownerId);
}
