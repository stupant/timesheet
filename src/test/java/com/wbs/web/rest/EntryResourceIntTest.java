package com.wbs.web.rest;

import com.wbs.TimesheetApp;

import com.wbs.domain.Entry;
import com.wbs.repository.EntryRepository;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EntryResource REST controller.
 *
 * @see EntryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TimesheetApp.class)
public class EntryResourceIntTest {

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    private static final Integer DEFAULT_HOUR = 0;
    private static final Integer UPDATED_HOUR = 1;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DAY = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restEntryMockMvc;

    private Entry entry;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EntryResource entryResource = new EntryResource(entryRepository);
        this.restEntryMockMvc = MockMvcBuilders.standaloneSetup(entryResource)
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
    public static Entry createEntity() {
        Entry entry = new Entry()
            .user(DEFAULT_USER)
            .hour(DEFAULT_HOUR)
            .notes(DEFAULT_NOTES)
            .category(DEFAULT_CATEGORY)
            .day(DEFAULT_DAY);
        return entry;
    }

    @Before
    public void initTest() {
        entryRepository.deleteAll();
        entry = createEntity();
    }

    @Test
    public void createEntry() throws Exception {
        int databaseSizeBeforeCreate = entryRepository.findAll().size();

        // Create the Entry
        restEntryMockMvc.perform(post("/api/entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entry)))
            .andExpect(status().isCreated());

        // Validate the Entry in the database
        List<Entry> entryList = entryRepository.findAll();
        assertThat(entryList).hasSize(databaseSizeBeforeCreate + 1);
        Entry testEntry = entryList.get(entryList.size() - 1);
        assertThat(testEntry.getUser()).isEqualTo(DEFAULT_USER);
        assertThat(testEntry.getHour()).isEqualTo(DEFAULT_HOUR);
        assertThat(testEntry.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testEntry.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testEntry.getDay()).isEqualTo(DEFAULT_DAY);
    }

    @Test
    public void createEntryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entryRepository.findAll().size();

        // Create the Entry with an existing ID
        entry.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntryMockMvc.perform(post("/api/entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entry)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Entry> entryList = entryRepository.findAll();
        assertThat(entryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkUserIsRequired() throws Exception {
        int databaseSizeBeforeTest = entryRepository.findAll().size();
        // set the field null
        entry.setUser(null);

        // Create the Entry, which fails.

        restEntryMockMvc.perform(post("/api/entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entry)))
            .andExpect(status().isBadRequest());

        List<Entry> entryList = entryRepository.findAll();
        assertThat(entryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkHourIsRequired() throws Exception {
        int databaseSizeBeforeTest = entryRepository.findAll().size();
        // set the field null
        entry.setHour(null);

        // Create the Entry, which fails.

        restEntryMockMvc.perform(post("/api/entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entry)))
            .andExpect(status().isBadRequest());

        List<Entry> entryList = entryRepository.findAll();
        assertThat(entryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = entryRepository.findAll().size();
        // set the field null
        entry.setCategory(null);

        // Create the Entry, which fails.

        restEntryMockMvc.perform(post("/api/entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entry)))
            .andExpect(status().isBadRequest());

        List<Entry> entryList = entryRepository.findAll();
        assertThat(entryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = entryRepository.findAll().size();
        // set the field null
        entry.setDay(null);

        // Create the Entry, which fails.

        restEntryMockMvc.perform(post("/api/entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entry)))
            .andExpect(status().isBadRequest());

        List<Entry> entryList = entryRepository.findAll();
        assertThat(entryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllEntries() throws Exception {
        // Initialize the database
        entryRepository.save(entry);

        // Get all the entryList
        restEntryMockMvc.perform(get("/api/entries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entry.getId())))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER.toString())))
            .andExpect(jsonPath("$.[*].hour").value(hasItem(DEFAULT_HOUR)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY.toString())));
    }

    @Test
    public void getEntry() throws Exception {
        // Initialize the database
        entryRepository.save(entry);

        // Get the entry
        restEntryMockMvc.perform(get("/api/entries/{id}", entry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entry.getId()))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER.toString()))
            .andExpect(jsonPath("$.hour").value(DEFAULT_HOUR))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY.toString()));
    }

    @Test
    public void getNonExistingEntry() throws Exception {
        // Get the entry
        restEntryMockMvc.perform(get("/api/entries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateEntry() throws Exception {
        // Initialize the database
        entryRepository.save(entry);
        int databaseSizeBeforeUpdate = entryRepository.findAll().size();

        // Update the entry
        Entry updatedEntry = entryRepository.findOne(entry.getId());
        updatedEntry
            .user(UPDATED_USER)
            .hour(UPDATED_HOUR)
            .notes(UPDATED_NOTES)
            .category(UPDATED_CATEGORY)
            .day(UPDATED_DAY);

        restEntryMockMvc.perform(put("/api/entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntry)))
            .andExpect(status().isOk());

        // Validate the Entry in the database
        List<Entry> entryList = entryRepository.findAll();
        assertThat(entryList).hasSize(databaseSizeBeforeUpdate);
        Entry testEntry = entryList.get(entryList.size() - 1);
        assertThat(testEntry.getUser()).isEqualTo(UPDATED_USER);
        assertThat(testEntry.getHour()).isEqualTo(UPDATED_HOUR);
        assertThat(testEntry.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testEntry.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testEntry.getDay()).isEqualTo(UPDATED_DAY);
    }

    @Test
    public void updateNonExistingEntry() throws Exception {
        int databaseSizeBeforeUpdate = entryRepository.findAll().size();

        // Create the Entry

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEntryMockMvc.perform(put("/api/entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entry)))
            .andExpect(status().isCreated());

        // Validate the Entry in the database
        List<Entry> entryList = entryRepository.findAll();
        assertThat(entryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteEntry() throws Exception {
        // Initialize the database
        entryRepository.save(entry);
        int databaseSizeBeforeDelete = entryRepository.findAll().size();

        // Get the entry
        restEntryMockMvc.perform(delete("/api/entries/{id}", entry.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Entry> entryList = entryRepository.findAll();
        assertThat(entryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Entry.class);
        Entry entry1 = new Entry();
        entry1.setId("id1");
        Entry entry2 = new Entry();
        entry2.setId(entry1.getId());
        assertThat(entry1).isEqualTo(entry2);
        entry2.setId("id2");
        assertThat(entry1).isNotEqualTo(entry2);
        entry1.setId(null);
        assertThat(entry1).isNotEqualTo(entry2);
    }
}
