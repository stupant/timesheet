package com.wbs.repository;

import com.wbs.domain.Timesheet;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Timesheet entity.
 */
@Repository
public interface TimesheetRepository extends MongoRepository<Timesheet,String> {

  Timesheet findOneByUserAndYearAndWeek(String user, int year, int week);
}
