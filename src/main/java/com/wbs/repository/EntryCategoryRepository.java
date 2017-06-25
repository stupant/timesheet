package com.wbs.repository;

import com.wbs.domain.EntryCategory;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the EntryCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntryCategoryRepository extends MongoRepository<EntryCategory,String> {
    
}
