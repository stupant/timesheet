package com.wbs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.wbs.domain.Timesheet;

import com.wbs.repository.TimesheetRepository;
import com.wbs.web.rest.util.HeaderUtil;
import com.wbs.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Timesheet.
 */
@RestController
@RequestMapping("/api")
public class TimesheetResource {

    private final Logger log = LoggerFactory.getLogger(TimesheetResource.class);

    private static final String ENTITY_NAME = "timesheet";

    private final TimesheetRepository timesheetRepository;

    public TimesheetResource(TimesheetRepository timesheetRepository) {
        this.timesheetRepository = timesheetRepository;
    }

    /**
     * POST  /timesheets : Create a new timesheet.
     *
     * @param timesheet the timesheet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new timesheet, or with status 400 (Bad Request) if the timesheet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/timesheets")
    @Timed
    public ResponseEntity<Timesheet> createTimesheet(@Valid @RequestBody Timesheet timesheet) throws URISyntaxException {
        log.debug("REST request to save Timesheet : {}", timesheet);
        if (timesheet.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new timesheet cannot already have an ID")).body(null);
        }
        Timesheet result = timesheetRepository.save(timesheet);
        return ResponseEntity.created(new URI("/api/timesheets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /timesheets : Updates an existing timesheet.
     *
     * @param timesheet the timesheet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated timesheet,
     * or with status 400 (Bad Request) if the timesheet is not valid,
     * or with status 500 (Internal Server Error) if the timesheet couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/timesheets")
    @Timed
    public ResponseEntity<Timesheet> updateTimesheet(@Valid @RequestBody Timesheet timesheet) throws URISyntaxException {
        log.debug("REST request to update Timesheet : {}", timesheet);
        if (timesheet.getId() == null) {
            return createTimesheet(timesheet);
        }
        Timesheet result = timesheetRepository.save(timesheet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, timesheet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /timesheets : get all the timesheets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of timesheets in body
     */
    @GetMapping("/timesheets")
    @Timed
    public ResponseEntity<List<Timesheet>> getAllTimesheets(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Timesheets");
        Page<Timesheet> page = timesheetRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/timesheets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /timesheets/:id : get the "id" timesheet.
     *
     * @param id the id of the timesheet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the timesheet, or with status 404 (Not Found)
     */
    @GetMapping("/timesheets/{id}")
    @Timed
    public ResponseEntity<Timesheet> getTimesheet(@PathVariable String id) {
        log.debug("REST request to get Timesheet : {}", id);
        Timesheet timesheet = timesheetRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(timesheet));
    }

    /**
     * DELETE  /timesheets/:id : delete the "id" timesheet.
     *
     * @param id the id of the timesheet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/timesheets/{id}")
    @Timed
    public ResponseEntity<Void> deleteTimesheet(@PathVariable String id) {
        log.debug("REST request to delete Timesheet : {}", id);
        timesheetRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
