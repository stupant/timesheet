package com.wbs.repository.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.wbs.domain.Entry;
import com.wbs.repository.EntryCustomRepository;

public class EntryRepositoryImpl implements EntryCustomRepository {
	@Autowired
    private MongoTemplate mongoTemplate;

    public List<Entry> lookup(String user, Date dateFrom, Date dateTo) {
    	Query query = new Query()
    			.addCriteria(Criteria.where("user").is(user))
    			.addCriteria(Criteria.where("day").gte(dateFrom).lt(dateTo))
    			
    					// .lt(dateTo)
    			;
    	return mongoTemplate.find(query, Entry.class);
	}

}
