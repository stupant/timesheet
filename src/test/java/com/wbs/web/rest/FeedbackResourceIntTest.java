package com.wbs.web.rest;

import com.wbs.TimesheetApp;

import com.wbs.domain.Feedback;
import com.wbs.repository.FeedbackRepository;
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
 * Test class for the FeedbackResource REST controller.
 *
 * @see FeedbackResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TimesheetApp.class)
public class FeedbackResourceIntTest {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restFeedbackMockMvc;

    private Feedback feedback;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FeedbackResource feedbackResource = new FeedbackResource(feedbackRepository);
        this.restFeedbackMockMvc = MockMvcBuilders.standaloneSetup(feedbackResource)
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
    public static Feedback createEntity() {
        Feedback feedback = new Feedback()
            .email(DEFAULT_EMAIL)
            .comment(DEFAULT_COMMENT)
            .time(DEFAULT_TIME);
        return feedback;
    }

    @Before
    public void initTest() {
        feedbackRepository.deleteAll();
        feedback = createEntity();
    }

    @Test
    public void createFeedback() throws Exception {
        int databaseSizeBeforeCreate = feedbackRepository.findAll().size();

        // Create the Feedback
        restFeedbackMockMvc.perform(post("/api/feedbacks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feedback)))
            .andExpect(status().isCreated());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeCreate + 1);
        Feedback testFeedback = feedbackList.get(feedbackList.size() - 1);
        assertThat(testFeedback.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testFeedback.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testFeedback.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    public void createFeedbackWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = feedbackRepository.findAll().size();

        // Create the Feedback with an existing ID
        feedback.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeedbackMockMvc.perform(post("/api/feedbacks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feedback)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedbackRepository.findAll().size();
        // set the field null
        feedback.setEmail(null);

        // Create the Feedback, which fails.

        restFeedbackMockMvc.perform(post("/api/feedbacks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feedback)))
            .andExpect(status().isBadRequest());

        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllFeedbacks() throws Exception {
        // Initialize the database
        feedbackRepository.save(feedback);

        // Get all the feedbackList
        restFeedbackMockMvc.perform(get("/api/feedbacks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feedback.getId())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(sameInstant(DEFAULT_TIME))));
    }

    @Test
    public void getFeedback() throws Exception {
        // Initialize the database
        feedbackRepository.save(feedback);

        // Get the feedback
        restFeedbackMockMvc.perform(get("/api/feedbacks/{id}", feedback.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(feedback.getId()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.time").value(sameInstant(DEFAULT_TIME)));
    }

    @Test
    public void getNonExistingFeedback() throws Exception {
        // Get the feedback
        restFeedbackMockMvc.perform(get("/api/feedbacks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateFeedback() throws Exception {
        // Initialize the database
        feedbackRepository.save(feedback);
        int databaseSizeBeforeUpdate = feedbackRepository.findAll().size();

        // Update the feedback
        Feedback updatedFeedback = feedbackRepository.findOne(feedback.getId());
        updatedFeedback
            .email(UPDATED_EMAIL)
            .comment(UPDATED_COMMENT)
            .time(UPDATED_TIME);

        restFeedbackMockMvc.perform(put("/api/feedbacks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFeedback)))
            .andExpect(status().isOk());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
        Feedback testFeedback = feedbackList.get(feedbackList.size() - 1);
        assertThat(testFeedback.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFeedback.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testFeedback.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    public void updateNonExistingFeedback() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRepository.findAll().size();

        // Create the Feedback

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFeedbackMockMvc.perform(put("/api/feedbacks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feedback)))
            .andExpect(status().isCreated());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteFeedback() throws Exception {
        // Initialize the database
        feedbackRepository.save(feedback);
        int databaseSizeBeforeDelete = feedbackRepository.findAll().size();

        // Get the feedback
        restFeedbackMockMvc.perform(delete("/api/feedbacks/{id}", feedback.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Feedback.class);
        Feedback feedback1 = new Feedback();
        feedback1.setId("id1");
        Feedback feedback2 = new Feedback();
        feedback2.setId(feedback1.getId());
        assertThat(feedback1).isEqualTo(feedback2);
        feedback2.setId("id2");
        assertThat(feedback1).isNotEqualTo(feedback2);
        feedback1.setId(null);
        assertThat(feedback1).isNotEqualTo(feedback2);
    }
}
