package com.olena.menucleanupservice.repository;

import com.olena.menucleanupservice.repository.entity.EventMenu;
import com.olena.menucleanupservice.repository.entity.MenuCleanupRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MenuCleanupRequestRepository extends MongoRepository<MenuCleanupRequest, String> {

    List<MenuCleanupRequest> findAllByUserNameOrderByLastmodifiedDesc(String userName);

}
