package com.sergio.socialnetwork.repositories;

import java.util.List;

import com.sergio.socialnetwork.entities.Image;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query(value = "SELECT img FROM Image img WHERE img.user.id IN :ids ORDER BY img.createdDate DESC")
    List<Image> findCommunityImages(@Param("ids") List<Long> ids, Pageable pageable);
}
