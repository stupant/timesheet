package com.wbs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.wbs.domain.Timesheet;

import com.wbs.repository.TimesheetRepository;
import com.wbs.service.PdfConverter;
import com.wbs.web.rest.util.HeaderUtil;
import com.wbs.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
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

    private final PdfConverter converter;

    public TimesheetResource(TimesheetRepository timesheetRepository, PdfConverter converter) {
        this.timesheetRepository = timesheetRepository;
        this.converter = converter;
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
     * or with status 500 (Internal Server Error) if the timesheet couldnt be updated
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
     * GET  /timesheets/:id : get the "id" timesheet.
     *
     * @param id the id of the timesheet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the timesheet, or with status 404 (Not Found)
     */
    @GetMapping("/timesheet-lookup/{user}/{year}/{week}")
    @Timed
    public ResponseEntity<Timesheet> getTimesheet(@PathVariable("user") String user, @PathVariable("year") int year, @PathVariable("week") int week) {
        log.debug("REST request to get Timesheet : user {} year {} week {}", user, year, week);
        Timesheet timesheet = timesheetRepository.findOneByUserAndYearAndWeek(user, year, week);
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
    
    /**
     * POST  /timesheets : Create a new timesheet.
     *
     * @param timesheet the timesheet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new timesheet, or with status 400 (Bad Request) if the timesheet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws DocumentException 
     * @throws IOException 
     */
    @PostMapping("/convertHtmlToPdf")
    @Timed
    public ResponseEntity<byte[]> exportPdfTimesheet(@Valid @RequestBody String html) throws URISyntaxException, IOException {
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	html = StringEscapeUtils.unescapeHtml4(html);
    	String css = FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("mails/style.css").getPath()), "UTF-8");
		converter.convertHtmlToPdf(html, css, bos);

        return ResponseEntity.created(new URI("/api/convertHtmlToPdf"))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, null))
            .body(Base64.getEncoder().encode(bos.toByteArray()));
    }
}
