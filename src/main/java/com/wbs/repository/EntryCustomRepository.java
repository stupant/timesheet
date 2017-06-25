package com.wbs.repository;

import java.util.Date;
import java.util.List;

import com.wbs.domain.Entry;

public interface EntryCustomRepository {

	List<Entry> lookup(String user, Date dateFrom, Date dateTo);
}
