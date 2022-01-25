package com.olena.menucleanupservice.repository;

import com.olena.menucleanupservice.repository.entity.EventMenu;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface EventMenuRepository extends MongoRepository<EventMenu, String> {

    EventMenu findFirstByEventId(UUID eventId);

    List<EventMenu> findAllByUserNameOrderByLastmodifiedDesc(String username);

}
