package com.sergio.socialnetwork.repositories;

import com.sergio.socialnetwork.entities.MongoUser;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<MongoUser, String> {

    Optional<MongoUser> findByLogin(String login);

    @Query("{$or: [{firstName: /?0/}, {lastName: /?0/}, {login: /?0/}]}")
    List<MongoUser> searchUsers(String term);

}
