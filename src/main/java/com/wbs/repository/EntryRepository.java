package com.wbs.repository;

import com.wbs.domain.Entry;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Entry entity.
 */
@Repository
public interface EntryRepository extends MongoRepository<Entry,String>, EntryCustomRepository {

	

}
