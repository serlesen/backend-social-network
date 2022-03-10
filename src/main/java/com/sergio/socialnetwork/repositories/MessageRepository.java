package com.sergio.socialnetwork.repositories;

import com.sergio.socialnetwork.entities.MongoMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MessageRepository extends PagingAndSortingRepository<MongoMessage, String> {

    List<MongoMessage> findAllByUserIdIn(List<String> ids, Pageable pageable);
}
