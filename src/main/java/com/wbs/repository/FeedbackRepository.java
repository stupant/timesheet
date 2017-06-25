package com.wbs.repository;

import com.wbs.domain.Feedback;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Feedback entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeedbackRepository extends MongoRepository<Feedback,String> {
    
}
