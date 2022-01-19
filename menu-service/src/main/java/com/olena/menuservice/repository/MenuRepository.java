package com.olena.menuservice.repository;

import com.olena.menuservice.repository.entity.EventMenu;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface MenuRepository extends MongoRepository<EventMenu, String> {

    EventMenu findFirstByEventId(UUID eventId);

}
