package com.wbs.web.rest;

import com.wbs.TimesheetApp;

import com.wbs.domain.Timesheet;
import com.wbs.repository.TimesheetRepository;
import com.wbs.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.wbs.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TimesheetResource REST controller.
 *
 * @see TimesheetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TimesheetApp.class)
public class TimesheetResourceIntTest {

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEEK = 0;
    private static final Integer UPDATED_WEEK = 1;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final ZonedDateTime DEFAULT_SUBMIT_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SUBMIT_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOTAL_HOURS = 1;
    private static final Integer UPDATED_TOTAL_HOURS = 2;

    @Autowired
    private TimesheetRepository timesheetRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restTimesheetMockMvc;

    private Timesheet timesheet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TimesheetResource timesheetResource = new TimesheetResource(timesheetRepository);
        this.restTimesheetMockMvc = MockMvcBuilders.standaloneSetup(timesheetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Timesheet createEntity() {
        Timesheet timesheet = new Timesheet()
            .user(DEFAULT_USER)
            .week(DEFAULT_WEEK)
            .year(DEFAULT_YEAR)
            .submitAt(DEFAULT_SUBMIT_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .summary(DEFAULT_SUMMARY)
            .totalHours(DEFAULT_TOTAL_HOURS);
        return timesheet;
    }

    @Before
    public void initTest() {
        timesheetRepository.deleteAll();
        timesheet = createEntity();
    }

    @Test
    public void createTimesheet() throws Exception {
        int databaseSizeBeforeCreate = timesheetRepository.findAll().size();

        // Create the Timesheet
        restTimesheetMockMvc.perform(post("/api/timesheets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timesheet)))
            .andExpect(status().isCreated());

        // Validate the Timesheet in the database
        List<Timesheet> timesheetList = timesheetRepository.findAll();
        assertThat(timesheetList).hasSize(databaseSizeBeforeCreate + 1);
        Timesheet testTimesheet = timesheetList.get(timesheetList.size() - 1);
        assertThat(testTimesheet.getUser()).isEqualTo(DEFAULT_USER);
        assertThat(testTimesheet.getWeek()).isEqualTo(DEFAULT_WEEK);
        assertThat(testTimesheet.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testTimesheet.getSubmitAt()).isEqualTo(DEFAULT_SUBMIT_AT);
        assertThat(testTimesheet.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testTimesheet.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testTimesheet.getSummary()).isEqualTo(DEFAULT_SUMMARY);
        assertThat(testTimesheet.getTotalHours()).isEqualTo(DEFAULT_TOTAL_HOURS);
    }

    @Test
    public void createTimesheetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = timesheetRepository.findAll().size();

        // Create the Timesheet with an existing ID
        timesheet.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimesheetMockMvc.perform(post("/api/timesheets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timesheet)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Timesheet> timesheetList = timesheetRepository.findAll();
        assertThat(timesheetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkUserIsRequired() throws Exception {
        int databaseSizeBeforeTest = timesheetRepository.findAll().size();
        // set the field null
        timesheet.setUser(null);

        // Create the Timesheet, which fails.

        restTimesheetMockMvc.perform(post("/api/timesheets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timesheet)))
            .andExpect(status().isBadRequest());

        List<Timesheet> timesheetList = timesheetRepository.findAll();
        assertThat(timesheetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkWeekIsRequired() throws Exception {
        int databaseSizeBeforeTest = timesheetRepository.findAll().size();
        // set the field null
        timesheet.setWeek(null);

        // Create the Timesheet, which fails.

        restTimesheetMockMvc.perform(post("/api/timesheets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timesheet)))
            .andExpect(status().isBadRequest());

        List<Timesheet> timesheetList = timesheetRepository.findAll();
        assertThat(timesheetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllTimesheets() throws Exception {
        // Initialize the database
        timesheetRepository.save(timesheet);

        // Get all the timesheetList
        restTimesheetMockMvc.perform(get("/api/timesheets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timesheet.getId())))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER.toString())))
            .andExpect(jsonPath("$.[*].week").value(hasItem(DEFAULT_WEEK)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].submitAt").value(hasItem(sameInstant(DEFAULT_SUBMIT_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY.toString())))
            .andExpect(jsonPath("$.[*].totalHours").value(hasItem(DEFAULT_TOTAL_HOURS)));
    }

    @Test
    public void getTimesheet() throws Exception {
        // Initialize the database
        timesheetRepository.save(timesheet);

        // Get the timesheet
        restTimesheetMockMvc.perform(get("/api/timesheets/{id}", timesheet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(timesheet.getId()))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER.toString()))
            .andExpect(jsonPath("$.week").value(DEFAULT_WEEK))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.submitAt").value(sameInstant(DEFAULT_SUBMIT_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY.toString()))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY.toString()))
            .andExpect(jsonPath("$.totalHours").value(DEFAULT_TOTAL_HOURS));
    }

    @Test
    public void getNonExistingTimesheet() throws Exception {
        // Get the timesheet
        restTimesheetMockMvc.perform(get("/api/timesheets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateTimesheet() throws Exception {
        // Initialize the database
        timesheetRepository.save(timesheet);
        int databaseSizeBeforeUpdate = timesheetRepository.findAll().size();

        // Update the timesheet
        Timesheet updatedTimesheet = timesheetRepository.findOne(timesheet.getId());
        updatedTimesheet
            .user(UPDATED_USER)
            .week(UPDATED_WEEK)
            .year(UPDATED_YEAR)
            .submitAt(UPDATED_SUBMIT_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .summary(UPDATED_SUMMARY)
            .totalHours(UPDATED_TOTAL_HOURS);

        restTimesheetMockMvc.perform(put("/api/timesheets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTimesheet)))
            .andExpect(status().isOk());

        // Validate the Timesheet in the database
        List<Timesheet> timesheetList = timesheetRepository.findAll();
        assertThat(timesheetList).hasSize(databaseSizeBeforeUpdate);
        Timesheet testTimesheet = timesheetList.get(timesheetList.size() - 1);
        assertThat(testTimesheet.getUser()).isEqualTo(UPDATED_USER);
        assertThat(testTimesheet.getWeek()).isEqualTo(UPDATED_WEEK);
        assertThat(testTimesheet.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testTimesheet.getSubmitAt()).isEqualTo(UPDATED_SUBMIT_AT);
        assertThat(testTimesheet.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testTimesheet.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testTimesheet.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testTimesheet.getTotalHours()).isEqualTo(UPDATED_TOTAL_HOURS);
    }

    @Test
    public void updateNonExistingTimesheet() throws Exception {
        int databaseSizeBeforeUpdate = timesheetRepository.findAll().size();

        // Create the Timesheet

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTimesheetMockMvc.perform(put("/api/timesheets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timesheet)))
            .andExpect(status().isCreated());

        // Validate the Timesheet in the database
        List<Timesheet> timesheetList = timesheetRepository.findAll();
        assertThat(timesheetList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteTimesheet() throws Exception {
        // Initialize the database
        timesheetRepository.save(timesheet);
        int databaseSizeBeforeDelete = timesheetRepository.findAll().size();

        // Get the timesheet
        restTimesheetMockMvc.perform(delete("/api/timesheets/{id}", timesheet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Timesheet> timesheetList = timesheetRepository.findAll();
        assertThat(timesheetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Timesheet.class);
        Timesheet timesheet1 = new Timesheet();
        timesheet1.setId("id1");
        Timesheet timesheet2 = new Timesheet();
        timesheet2.setId(timesheet1.getId());
        assertThat(timesheet1).isEqualTo(timesheet2);
        timesheet2.setId("id2");
        assertThat(timesheet1).isNotEqualTo(timesheet2);
        timesheet1.setId(null);
        assertThat(timesheet1).isNotEqualTo(timesheet2);
    }
}
