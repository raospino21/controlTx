package co.com.ies.smol.web.rest;

import static co.com.ies.smol.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.DataSheetInterface;
import co.com.ies.smol.repository.DataSheetInterfaceRepository;
import co.com.ies.smol.service.criteria.DataSheetInterfaceCriteria;
import co.com.ies.smol.service.dto.DataSheetInterfaceDTO;
import co.com.ies.smol.service.mapper.DataSheetInterfaceMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DataSheetInterfaceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DataSheetInterfaceResourceIT {

    private static final Long DEFAULT_COLCIRCUITOS_LOT_NUMBER = 1L;
    private static final Long UPDATED_COLCIRCUITOS_LOT_NUMBER = 2L;
    private static final Long SMALLER_COLCIRCUITOS_LOT_NUMBER = 1L - 1L;

    private static final Long DEFAULT_ORDER_AMOUNT = 1L;
    private static final Long UPDATED_ORDER_AMOUNT = 2L;
    private static final Long SMALLER_ORDER_AMOUNT = 1L - 1L;

    private static final Long DEFAULT_AMOUNT_RECEIVED = 1L;
    private static final Long UPDATED_AMOUNT_RECEIVED = 2L;
    private static final Long SMALLER_AMOUNT_RECEIVED = 1L - 1L;

    private static final String DEFAULT_REMISSION = "AAAAAAAAAA";
    private static final String UPDATED_REMISSION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_ENTRY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ENTRY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_ENTRY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Long DEFAULT_IES_ORDER_NUMBER = 1L;
    private static final Long UPDATED_IES_ORDER_NUMBER = 2L;
    private static final Long SMALLER_IES_ORDER_NUMBER = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/data-sheet-interfaces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DataSheetInterfaceRepository dataSheetInterfaceRepository;

    @Autowired
    private DataSheetInterfaceMapper dataSheetInterfaceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDataSheetInterfaceMockMvc;

    private DataSheetInterface dataSheetInterface;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataSheetInterface createEntity(EntityManager em) {
        DataSheetInterface dataSheetInterface = new DataSheetInterface()
            .colcircuitosLotNumber(DEFAULT_COLCIRCUITOS_LOT_NUMBER)
            .orderAmount(DEFAULT_ORDER_AMOUNT)
            .amountReceived(DEFAULT_AMOUNT_RECEIVED)
            .remission(DEFAULT_REMISSION)
            .entryDate(DEFAULT_ENTRY_DATE)
            .iesOrderNumber(DEFAULT_IES_ORDER_NUMBER);
        return dataSheetInterface;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataSheetInterface createUpdatedEntity(EntityManager em) {
        DataSheetInterface dataSheetInterface = new DataSheetInterface()
            .colcircuitosLotNumber(UPDATED_COLCIRCUITOS_LOT_NUMBER)
            .orderAmount(UPDATED_ORDER_AMOUNT)
            .amountReceived(UPDATED_AMOUNT_RECEIVED)
            .remission(UPDATED_REMISSION)
            .entryDate(UPDATED_ENTRY_DATE)
            .iesOrderNumber(UPDATED_IES_ORDER_NUMBER);
        return dataSheetInterface;
    }

    @BeforeEach
    public void initTest() {
        dataSheetInterface = createEntity(em);
    }

    @Test
    @Transactional
    void createDataSheetInterface() throws Exception {
        int databaseSizeBeforeCreate = dataSheetInterfaceRepository.findAll().size();
        // Create the DataSheetInterface
        DataSheetInterfaceDTO dataSheetInterfaceDTO = dataSheetInterfaceMapper.toDto(dataSheetInterface);
        restDataSheetInterfaceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataSheetInterfaceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DataSheetInterface in the database
        List<DataSheetInterface> dataSheetInterfaceList = dataSheetInterfaceRepository.findAll();
        assertThat(dataSheetInterfaceList).hasSize(databaseSizeBeforeCreate + 1);
        DataSheetInterface testDataSheetInterface = dataSheetInterfaceList.get(dataSheetInterfaceList.size() - 1);
        assertThat(testDataSheetInterface.getColcircuitosLotNumber()).isEqualTo(DEFAULT_COLCIRCUITOS_LOT_NUMBER);
        assertThat(testDataSheetInterface.getOrderAmount()).isEqualTo(DEFAULT_ORDER_AMOUNT);
        assertThat(testDataSheetInterface.getAmountReceived()).isEqualTo(DEFAULT_AMOUNT_RECEIVED);
        assertThat(testDataSheetInterface.getRemission()).isEqualTo(DEFAULT_REMISSION);
        assertThat(testDataSheetInterface.getEntryDate()).isEqualTo(DEFAULT_ENTRY_DATE);
        assertThat(testDataSheetInterface.getIesOrderNumber()).isEqualTo(DEFAULT_IES_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void createDataSheetInterfaceWithExistingId() throws Exception {
        // Create the DataSheetInterface with an existing ID
        dataSheetInterface.setId(1L);
        DataSheetInterfaceDTO dataSheetInterfaceDTO = dataSheetInterfaceMapper.toDto(dataSheetInterface);

        int databaseSizeBeforeCreate = dataSheetInterfaceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataSheetInterfaceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataSheetInterfaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataSheetInterface in the database
        List<DataSheetInterface> dataSheetInterfaceList = dataSheetInterfaceRepository.findAll();
        assertThat(dataSheetInterfaceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkColcircuitosLotNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataSheetInterfaceRepository.findAll().size();
        // set the field null
        dataSheetInterface.setColcircuitosLotNumber(null);

        // Create the DataSheetInterface, which fails.
        DataSheetInterfaceDTO dataSheetInterfaceDTO = dataSheetInterfaceMapper.toDto(dataSheetInterface);

        restDataSheetInterfaceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataSheetInterfaceDTO))
            )
            .andExpect(status().isBadRequest());

        List<DataSheetInterface> dataSheetInterfaceList = dataSheetInterfaceRepository.findAll();
        assertThat(dataSheetInterfaceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrderAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataSheetInterfaceRepository.findAll().size();
        // set the field null
        dataSheetInterface.setOrderAmount(null);

        // Create the DataSheetInterface, which fails.
        DataSheetInterfaceDTO dataSheetInterfaceDTO = dataSheetInterfaceMapper.toDto(dataSheetInterface);

        restDataSheetInterfaceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataSheetInterfaceDTO))
            )
            .andExpect(status().isBadRequest());

        List<DataSheetInterface> dataSheetInterfaceList = dataSheetInterfaceRepository.findAll();
        assertThat(dataSheetInterfaceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountReceivedIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataSheetInterfaceRepository.findAll().size();
        // set the field null
        dataSheetInterface.setAmountReceived(null);

        // Create the DataSheetInterface, which fails.
        DataSheetInterfaceDTO dataSheetInterfaceDTO = dataSheetInterfaceMapper.toDto(dataSheetInterface);

        restDataSheetInterfaceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataSheetInterfaceDTO))
            )
            .andExpect(status().isBadRequest());

        List<DataSheetInterface> dataSheetInterfaceList = dataSheetInterfaceRepository.findAll();
        assertThat(dataSheetInterfaceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRemissionIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataSheetInterfaceRepository.findAll().size();
        // set the field null
        dataSheetInterface.setRemission(null);

        // Create the DataSheetInterface, which fails.
        DataSheetInterfaceDTO dataSheetInterfaceDTO = dataSheetInterfaceMapper.toDto(dataSheetInterface);

        restDataSheetInterfaceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataSheetInterfaceDTO))
            )
            .andExpect(status().isBadRequest());

        List<DataSheetInterface> dataSheetInterfaceList = dataSheetInterfaceRepository.findAll();
        assertThat(dataSheetInterfaceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntryDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataSheetInterfaceRepository.findAll().size();
        // set the field null
        dataSheetInterface.setEntryDate(null);

        // Create the DataSheetInterface, which fails.
        DataSheetInterfaceDTO dataSheetInterfaceDTO = dataSheetInterfaceMapper.toDto(dataSheetInterface);

        restDataSheetInterfaceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataSheetInterfaceDTO))
            )
            .andExpect(status().isBadRequest());

        List<DataSheetInterface> dataSheetInterfaceList = dataSheetInterfaceRepository.findAll();
        assertThat(dataSheetInterfaceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIesOrderNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataSheetInterfaceRepository.findAll().size();
        // set the field null
        dataSheetInterface.setIesOrderNumber(null);

        // Create the DataSheetInterface, which fails.
        DataSheetInterfaceDTO dataSheetInterfaceDTO = dataSheetInterfaceMapper.toDto(dataSheetInterface);

        restDataSheetInterfaceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataSheetInterfaceDTO))
            )
            .andExpect(status().isBadRequest());

        List<DataSheetInterface> dataSheetInterfaceList = dataSheetInterfaceRepository.findAll();
        assertThat(dataSheetInterfaceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfaces() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList
        restDataSheetInterfaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataSheetInterface.getId().intValue())))
            .andExpect(jsonPath("$.[*].colcircuitosLotNumber").value(hasItem(DEFAULT_COLCIRCUITOS_LOT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].orderAmount").value(hasItem(DEFAULT_ORDER_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].amountReceived").value(hasItem(DEFAULT_AMOUNT_RECEIVED.intValue())))
            .andExpect(jsonPath("$.[*].remission").value(hasItem(DEFAULT_REMISSION)))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(sameInstant(DEFAULT_ENTRY_DATE))))
            .andExpect(jsonPath("$.[*].iesOrderNumber").value(hasItem(DEFAULT_IES_ORDER_NUMBER.intValue())));
    }

    @Test
    @Transactional
    void getDataSheetInterface() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get the dataSheetInterface
        restDataSheetInterfaceMockMvc
            .perform(get(ENTITY_API_URL_ID, dataSheetInterface.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dataSheetInterface.getId().intValue()))
            .andExpect(jsonPath("$.colcircuitosLotNumber").value(DEFAULT_COLCIRCUITOS_LOT_NUMBER.intValue()))
            .andExpect(jsonPath("$.orderAmount").value(DEFAULT_ORDER_AMOUNT.intValue()))
            .andExpect(jsonPath("$.amountReceived").value(DEFAULT_AMOUNT_RECEIVED.intValue()))
            .andExpect(jsonPath("$.remission").value(DEFAULT_REMISSION))
            .andExpect(jsonPath("$.entryDate").value(sameInstant(DEFAULT_ENTRY_DATE)))
            .andExpect(jsonPath("$.iesOrderNumber").value(DEFAULT_IES_ORDER_NUMBER.intValue()));
    }

    @Test
    @Transactional
    void getDataSheetInterfacesByIdFiltering() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        Long id = dataSheetInterface.getId();

        defaultDataSheetInterfaceShouldBeFound("id.equals=" + id);
        defaultDataSheetInterfaceShouldNotBeFound("id.notEquals=" + id);

        defaultDataSheetInterfaceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDataSheetInterfaceShouldNotBeFound("id.greaterThan=" + id);

        defaultDataSheetInterfaceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDataSheetInterfaceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByColcircuitosLotNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where colcircuitosLotNumber equals to DEFAULT_COLCIRCUITOS_LOT_NUMBER
        defaultDataSheetInterfaceShouldBeFound("colcircuitosLotNumber.equals=" + DEFAULT_COLCIRCUITOS_LOT_NUMBER);

        // Get all the dataSheetInterfaceList where colcircuitosLotNumber equals to UPDATED_COLCIRCUITOS_LOT_NUMBER
        defaultDataSheetInterfaceShouldNotBeFound("colcircuitosLotNumber.equals=" + UPDATED_COLCIRCUITOS_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByColcircuitosLotNumberIsInShouldWork() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where colcircuitosLotNumber in DEFAULT_COLCIRCUITOS_LOT_NUMBER or UPDATED_COLCIRCUITOS_LOT_NUMBER
        defaultDataSheetInterfaceShouldBeFound(
            "colcircuitosLotNumber.in=" + DEFAULT_COLCIRCUITOS_LOT_NUMBER + "," + UPDATED_COLCIRCUITOS_LOT_NUMBER
        );

        // Get all the dataSheetInterfaceList where colcircuitosLotNumber equals to UPDATED_COLCIRCUITOS_LOT_NUMBER
        defaultDataSheetInterfaceShouldNotBeFound("colcircuitosLotNumber.in=" + UPDATED_COLCIRCUITOS_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByColcircuitosLotNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where colcircuitosLotNumber is not null
        defaultDataSheetInterfaceShouldBeFound("colcircuitosLotNumber.specified=true");

        // Get all the dataSheetInterfaceList where colcircuitosLotNumber is null
        defaultDataSheetInterfaceShouldNotBeFound("colcircuitosLotNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByColcircuitosLotNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where colcircuitosLotNumber is greater than or equal to DEFAULT_COLCIRCUITOS_LOT_NUMBER
        defaultDataSheetInterfaceShouldBeFound("colcircuitosLotNumber.greaterThanOrEqual=" + DEFAULT_COLCIRCUITOS_LOT_NUMBER);

        // Get all the dataSheetInterfaceList where colcircuitosLotNumber is greater than or equal to UPDATED_COLCIRCUITOS_LOT_NUMBER
        defaultDataSheetInterfaceShouldNotBeFound("colcircuitosLotNumber.greaterThanOrEqual=" + UPDATED_COLCIRCUITOS_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByColcircuitosLotNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where colcircuitosLotNumber is less than or equal to DEFAULT_COLCIRCUITOS_LOT_NUMBER
        defaultDataSheetInterfaceShouldBeFound("colcircuitosLotNumber.lessThanOrEqual=" + DEFAULT_COLCIRCUITOS_LOT_NUMBER);

        // Get all the dataSheetInterfaceList where colcircuitosLotNumber is less than or equal to SMALLER_COLCIRCUITOS_LOT_NUMBER
        defaultDataSheetInterfaceShouldNotBeFound("colcircuitosLotNumber.lessThanOrEqual=" + SMALLER_COLCIRCUITOS_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByColcircuitosLotNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where colcircuitosLotNumber is less than DEFAULT_COLCIRCUITOS_LOT_NUMBER
        defaultDataSheetInterfaceShouldNotBeFound("colcircuitosLotNumber.lessThan=" + DEFAULT_COLCIRCUITOS_LOT_NUMBER);

        // Get all the dataSheetInterfaceList where colcircuitosLotNumber is less than UPDATED_COLCIRCUITOS_LOT_NUMBER
        defaultDataSheetInterfaceShouldBeFound("colcircuitosLotNumber.lessThan=" + UPDATED_COLCIRCUITOS_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByColcircuitosLotNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where colcircuitosLotNumber is greater than DEFAULT_COLCIRCUITOS_LOT_NUMBER
        defaultDataSheetInterfaceShouldNotBeFound("colcircuitosLotNumber.greaterThan=" + DEFAULT_COLCIRCUITOS_LOT_NUMBER);

        // Get all the dataSheetInterfaceList where colcircuitosLotNumber is greater than SMALLER_COLCIRCUITOS_LOT_NUMBER
        defaultDataSheetInterfaceShouldBeFound("colcircuitosLotNumber.greaterThan=" + SMALLER_COLCIRCUITOS_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByOrderAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where orderAmount equals to DEFAULT_ORDER_AMOUNT
        defaultDataSheetInterfaceShouldBeFound("orderAmount.equals=" + DEFAULT_ORDER_AMOUNT);

        // Get all the dataSheetInterfaceList where orderAmount equals to UPDATED_ORDER_AMOUNT
        defaultDataSheetInterfaceShouldNotBeFound("orderAmount.equals=" + UPDATED_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByOrderAmountIsInShouldWork() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where orderAmount in DEFAULT_ORDER_AMOUNT or UPDATED_ORDER_AMOUNT
        defaultDataSheetInterfaceShouldBeFound("orderAmount.in=" + DEFAULT_ORDER_AMOUNT + "," + UPDATED_ORDER_AMOUNT);

        // Get all the dataSheetInterfaceList where orderAmount equals to UPDATED_ORDER_AMOUNT
        defaultDataSheetInterfaceShouldNotBeFound("orderAmount.in=" + UPDATED_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByOrderAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where orderAmount is not null
        defaultDataSheetInterfaceShouldBeFound("orderAmount.specified=true");

        // Get all the dataSheetInterfaceList where orderAmount is null
        defaultDataSheetInterfaceShouldNotBeFound("orderAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByOrderAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where orderAmount is greater than or equal to DEFAULT_ORDER_AMOUNT
        defaultDataSheetInterfaceShouldBeFound("orderAmount.greaterThanOrEqual=" + DEFAULT_ORDER_AMOUNT);

        // Get all the dataSheetInterfaceList where orderAmount is greater than or equal to UPDATED_ORDER_AMOUNT
        defaultDataSheetInterfaceShouldNotBeFound("orderAmount.greaterThanOrEqual=" + UPDATED_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByOrderAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where orderAmount is less than or equal to DEFAULT_ORDER_AMOUNT
        defaultDataSheetInterfaceShouldBeFound("orderAmount.lessThanOrEqual=" + DEFAULT_ORDER_AMOUNT);

        // Get all the dataSheetInterfaceList where orderAmount is less than or equal to SMALLER_ORDER_AMOUNT
        defaultDataSheetInterfaceShouldNotBeFound("orderAmount.lessThanOrEqual=" + SMALLER_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByOrderAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where orderAmount is less than DEFAULT_ORDER_AMOUNT
        defaultDataSheetInterfaceShouldNotBeFound("orderAmount.lessThan=" + DEFAULT_ORDER_AMOUNT);

        // Get all the dataSheetInterfaceList where orderAmount is less than UPDATED_ORDER_AMOUNT
        defaultDataSheetInterfaceShouldBeFound("orderAmount.lessThan=" + UPDATED_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByOrderAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where orderAmount is greater than DEFAULT_ORDER_AMOUNT
        defaultDataSheetInterfaceShouldNotBeFound("orderAmount.greaterThan=" + DEFAULT_ORDER_AMOUNT);

        // Get all the dataSheetInterfaceList where orderAmount is greater than SMALLER_ORDER_AMOUNT
        defaultDataSheetInterfaceShouldBeFound("orderAmount.greaterThan=" + SMALLER_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByAmountReceivedIsEqualToSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where amountReceived equals to DEFAULT_AMOUNT_RECEIVED
        defaultDataSheetInterfaceShouldBeFound("amountReceived.equals=" + DEFAULT_AMOUNT_RECEIVED);

        // Get all the dataSheetInterfaceList where amountReceived equals to UPDATED_AMOUNT_RECEIVED
        defaultDataSheetInterfaceShouldNotBeFound("amountReceived.equals=" + UPDATED_AMOUNT_RECEIVED);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByAmountReceivedIsInShouldWork() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where amountReceived in DEFAULT_AMOUNT_RECEIVED or UPDATED_AMOUNT_RECEIVED
        defaultDataSheetInterfaceShouldBeFound("amountReceived.in=" + DEFAULT_AMOUNT_RECEIVED + "," + UPDATED_AMOUNT_RECEIVED);

        // Get all the dataSheetInterfaceList where amountReceived equals to UPDATED_AMOUNT_RECEIVED
        defaultDataSheetInterfaceShouldNotBeFound("amountReceived.in=" + UPDATED_AMOUNT_RECEIVED);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByAmountReceivedIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where amountReceived is not null
        defaultDataSheetInterfaceShouldBeFound("amountReceived.specified=true");

        // Get all the dataSheetInterfaceList where amountReceived is null
        defaultDataSheetInterfaceShouldNotBeFound("amountReceived.specified=false");
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByAmountReceivedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where amountReceived is greater than or equal to DEFAULT_AMOUNT_RECEIVED
        defaultDataSheetInterfaceShouldBeFound("amountReceived.greaterThanOrEqual=" + DEFAULT_AMOUNT_RECEIVED);

        // Get all the dataSheetInterfaceList where amountReceived is greater than or equal to UPDATED_AMOUNT_RECEIVED
        defaultDataSheetInterfaceShouldNotBeFound("amountReceived.greaterThanOrEqual=" + UPDATED_AMOUNT_RECEIVED);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByAmountReceivedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where amountReceived is less than or equal to DEFAULT_AMOUNT_RECEIVED
        defaultDataSheetInterfaceShouldBeFound("amountReceived.lessThanOrEqual=" + DEFAULT_AMOUNT_RECEIVED);

        // Get all the dataSheetInterfaceList where amountReceived is less than or equal to SMALLER_AMOUNT_RECEIVED
        defaultDataSheetInterfaceShouldNotBeFound("amountReceived.lessThanOrEqual=" + SMALLER_AMOUNT_RECEIVED);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByAmountReceivedIsLessThanSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where amountReceived is less than DEFAULT_AMOUNT_RECEIVED
        defaultDataSheetInterfaceShouldNotBeFound("amountReceived.lessThan=" + DEFAULT_AMOUNT_RECEIVED);

        // Get all the dataSheetInterfaceList where amountReceived is less than UPDATED_AMOUNT_RECEIVED
        defaultDataSheetInterfaceShouldBeFound("amountReceived.lessThan=" + UPDATED_AMOUNT_RECEIVED);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByAmountReceivedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where amountReceived is greater than DEFAULT_AMOUNT_RECEIVED
        defaultDataSheetInterfaceShouldNotBeFound("amountReceived.greaterThan=" + DEFAULT_AMOUNT_RECEIVED);

        // Get all the dataSheetInterfaceList where amountReceived is greater than SMALLER_AMOUNT_RECEIVED
        defaultDataSheetInterfaceShouldBeFound("amountReceived.greaterThan=" + SMALLER_AMOUNT_RECEIVED);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByRemissionIsEqualToSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where remission equals to DEFAULT_REMISSION
        defaultDataSheetInterfaceShouldBeFound("remission.equals=" + DEFAULT_REMISSION);

        // Get all the dataSheetInterfaceList where remission equals to UPDATED_REMISSION
        defaultDataSheetInterfaceShouldNotBeFound("remission.equals=" + UPDATED_REMISSION);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByRemissionIsInShouldWork() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where remission in DEFAULT_REMISSION or UPDATED_REMISSION
        defaultDataSheetInterfaceShouldBeFound("remission.in=" + DEFAULT_REMISSION + "," + UPDATED_REMISSION);

        // Get all the dataSheetInterfaceList where remission equals to UPDATED_REMISSION
        defaultDataSheetInterfaceShouldNotBeFound("remission.in=" + UPDATED_REMISSION);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByRemissionIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where remission is not null
        defaultDataSheetInterfaceShouldBeFound("remission.specified=true");

        // Get all the dataSheetInterfaceList where remission is null
        defaultDataSheetInterfaceShouldNotBeFound("remission.specified=false");
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByRemissionContainsSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where remission contains DEFAULT_REMISSION
        defaultDataSheetInterfaceShouldBeFound("remission.contains=" + DEFAULT_REMISSION);

        // Get all the dataSheetInterfaceList where remission contains UPDATED_REMISSION
        defaultDataSheetInterfaceShouldNotBeFound("remission.contains=" + UPDATED_REMISSION);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByRemissionNotContainsSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where remission does not contain DEFAULT_REMISSION
        defaultDataSheetInterfaceShouldNotBeFound("remission.doesNotContain=" + DEFAULT_REMISSION);

        // Get all the dataSheetInterfaceList where remission does not contain UPDATED_REMISSION
        defaultDataSheetInterfaceShouldBeFound("remission.doesNotContain=" + UPDATED_REMISSION);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByEntryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where entryDate equals to DEFAULT_ENTRY_DATE
        defaultDataSheetInterfaceShouldBeFound("entryDate.equals=" + DEFAULT_ENTRY_DATE);

        // Get all the dataSheetInterfaceList where entryDate equals to UPDATED_ENTRY_DATE
        defaultDataSheetInterfaceShouldNotBeFound("entryDate.equals=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByEntryDateIsInShouldWork() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where entryDate in DEFAULT_ENTRY_DATE or UPDATED_ENTRY_DATE
        defaultDataSheetInterfaceShouldBeFound("entryDate.in=" + DEFAULT_ENTRY_DATE + "," + UPDATED_ENTRY_DATE);

        // Get all the dataSheetInterfaceList where entryDate equals to UPDATED_ENTRY_DATE
        defaultDataSheetInterfaceShouldNotBeFound("entryDate.in=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByEntryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where entryDate is not null
        defaultDataSheetInterfaceShouldBeFound("entryDate.specified=true");

        // Get all the dataSheetInterfaceList where entryDate is null
        defaultDataSheetInterfaceShouldNotBeFound("entryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByEntryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where entryDate is greater than or equal to DEFAULT_ENTRY_DATE
        defaultDataSheetInterfaceShouldBeFound("entryDate.greaterThanOrEqual=" + DEFAULT_ENTRY_DATE);

        // Get all the dataSheetInterfaceList where entryDate is greater than or equal to UPDATED_ENTRY_DATE
        defaultDataSheetInterfaceShouldNotBeFound("entryDate.greaterThanOrEqual=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByEntryDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where entryDate is less than or equal to DEFAULT_ENTRY_DATE
        defaultDataSheetInterfaceShouldBeFound("entryDate.lessThanOrEqual=" + DEFAULT_ENTRY_DATE);

        // Get all the dataSheetInterfaceList where entryDate is less than or equal to SMALLER_ENTRY_DATE
        defaultDataSheetInterfaceShouldNotBeFound("entryDate.lessThanOrEqual=" + SMALLER_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByEntryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where entryDate is less than DEFAULT_ENTRY_DATE
        defaultDataSheetInterfaceShouldNotBeFound("entryDate.lessThan=" + DEFAULT_ENTRY_DATE);

        // Get all the dataSheetInterfaceList where entryDate is less than UPDATED_ENTRY_DATE
        defaultDataSheetInterfaceShouldBeFound("entryDate.lessThan=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByEntryDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where entryDate is greater than DEFAULT_ENTRY_DATE
        defaultDataSheetInterfaceShouldNotBeFound("entryDate.greaterThan=" + DEFAULT_ENTRY_DATE);

        // Get all the dataSheetInterfaceList where entryDate is greater than SMALLER_ENTRY_DATE
        defaultDataSheetInterfaceShouldBeFound("entryDate.greaterThan=" + SMALLER_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByIesOrderNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where iesOrderNumber equals to DEFAULT_IES_ORDER_NUMBER
        defaultDataSheetInterfaceShouldBeFound("iesOrderNumber.equals=" + DEFAULT_IES_ORDER_NUMBER);

        // Get all the dataSheetInterfaceList where iesOrderNumber equals to UPDATED_IES_ORDER_NUMBER
        defaultDataSheetInterfaceShouldNotBeFound("iesOrderNumber.equals=" + UPDATED_IES_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByIesOrderNumberIsInShouldWork() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where iesOrderNumber in DEFAULT_IES_ORDER_NUMBER or UPDATED_IES_ORDER_NUMBER
        defaultDataSheetInterfaceShouldBeFound("iesOrderNumber.in=" + DEFAULT_IES_ORDER_NUMBER + "," + UPDATED_IES_ORDER_NUMBER);

        // Get all the dataSheetInterfaceList where iesOrderNumber equals to UPDATED_IES_ORDER_NUMBER
        defaultDataSheetInterfaceShouldNotBeFound("iesOrderNumber.in=" + UPDATED_IES_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByIesOrderNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where iesOrderNumber is not null
        defaultDataSheetInterfaceShouldBeFound("iesOrderNumber.specified=true");

        // Get all the dataSheetInterfaceList where iesOrderNumber is null
        defaultDataSheetInterfaceShouldNotBeFound("iesOrderNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByIesOrderNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where iesOrderNumber is greater than or equal to DEFAULT_IES_ORDER_NUMBER
        defaultDataSheetInterfaceShouldBeFound("iesOrderNumber.greaterThanOrEqual=" + DEFAULT_IES_ORDER_NUMBER);

        // Get all the dataSheetInterfaceList where iesOrderNumber is greater than or equal to UPDATED_IES_ORDER_NUMBER
        defaultDataSheetInterfaceShouldNotBeFound("iesOrderNumber.greaterThanOrEqual=" + UPDATED_IES_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByIesOrderNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where iesOrderNumber is less than or equal to DEFAULT_IES_ORDER_NUMBER
        defaultDataSheetInterfaceShouldBeFound("iesOrderNumber.lessThanOrEqual=" + DEFAULT_IES_ORDER_NUMBER);

        // Get all the dataSheetInterfaceList where iesOrderNumber is less than or equal to SMALLER_IES_ORDER_NUMBER
        defaultDataSheetInterfaceShouldNotBeFound("iesOrderNumber.lessThanOrEqual=" + SMALLER_IES_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByIesOrderNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where iesOrderNumber is less than DEFAULT_IES_ORDER_NUMBER
        defaultDataSheetInterfaceShouldNotBeFound("iesOrderNumber.lessThan=" + DEFAULT_IES_ORDER_NUMBER);

        // Get all the dataSheetInterfaceList where iesOrderNumber is less than UPDATED_IES_ORDER_NUMBER
        defaultDataSheetInterfaceShouldBeFound("iesOrderNumber.lessThan=" + UPDATED_IES_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllDataSheetInterfacesByIesOrderNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        // Get all the dataSheetInterfaceList where iesOrderNumber is greater than DEFAULT_IES_ORDER_NUMBER
        defaultDataSheetInterfaceShouldNotBeFound("iesOrderNumber.greaterThan=" + DEFAULT_IES_ORDER_NUMBER);

        // Get all the dataSheetInterfaceList where iesOrderNumber is greater than SMALLER_IES_ORDER_NUMBER
        defaultDataSheetInterfaceShouldBeFound("iesOrderNumber.greaterThan=" + SMALLER_IES_ORDER_NUMBER);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDataSheetInterfaceShouldBeFound(String filter) throws Exception {
        restDataSheetInterfaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataSheetInterface.getId().intValue())))
            .andExpect(jsonPath("$.[*].colcircuitosLotNumber").value(hasItem(DEFAULT_COLCIRCUITOS_LOT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].orderAmount").value(hasItem(DEFAULT_ORDER_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].amountReceived").value(hasItem(DEFAULT_AMOUNT_RECEIVED.intValue())))
            .andExpect(jsonPath("$.[*].remission").value(hasItem(DEFAULT_REMISSION)))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(sameInstant(DEFAULT_ENTRY_DATE))))
            .andExpect(jsonPath("$.[*].iesOrderNumber").value(hasItem(DEFAULT_IES_ORDER_NUMBER.intValue())));

        // Check, that the count call also returns 1
        restDataSheetInterfaceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDataSheetInterfaceShouldNotBeFound(String filter) throws Exception {
        restDataSheetInterfaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDataSheetInterfaceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDataSheetInterface() throws Exception {
        // Get the dataSheetInterface
        restDataSheetInterfaceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDataSheetInterface() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        int databaseSizeBeforeUpdate = dataSheetInterfaceRepository.findAll().size();

        // Update the dataSheetInterface
        DataSheetInterface updatedDataSheetInterface = dataSheetInterfaceRepository.findById(dataSheetInterface.getId()).get();
        // Disconnect from session so that the updates on updatedDataSheetInterface are not directly saved in db
        em.detach(updatedDataSheetInterface);
        updatedDataSheetInterface
            .colcircuitosLotNumber(UPDATED_COLCIRCUITOS_LOT_NUMBER)
            .orderAmount(UPDATED_ORDER_AMOUNT)
            .amountReceived(UPDATED_AMOUNT_RECEIVED)
            .remission(UPDATED_REMISSION)
            .entryDate(UPDATED_ENTRY_DATE)
            .iesOrderNumber(UPDATED_IES_ORDER_NUMBER);
        DataSheetInterfaceDTO dataSheetInterfaceDTO = dataSheetInterfaceMapper.toDto(updatedDataSheetInterface);

        restDataSheetInterfaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dataSheetInterfaceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataSheetInterfaceDTO))
            )
            .andExpect(status().isOk());

        // Validate the DataSheetInterface in the database
        List<DataSheetInterface> dataSheetInterfaceList = dataSheetInterfaceRepository.findAll();
        assertThat(dataSheetInterfaceList).hasSize(databaseSizeBeforeUpdate);
        DataSheetInterface testDataSheetInterface = dataSheetInterfaceList.get(dataSheetInterfaceList.size() - 1);
        assertThat(testDataSheetInterface.getColcircuitosLotNumber()).isEqualTo(UPDATED_COLCIRCUITOS_LOT_NUMBER);
        assertThat(testDataSheetInterface.getOrderAmount()).isEqualTo(UPDATED_ORDER_AMOUNT);
        assertThat(testDataSheetInterface.getAmountReceived()).isEqualTo(UPDATED_AMOUNT_RECEIVED);
        assertThat(testDataSheetInterface.getRemission()).isEqualTo(UPDATED_REMISSION);
        assertThat(testDataSheetInterface.getEntryDate()).isEqualTo(UPDATED_ENTRY_DATE);
        assertThat(testDataSheetInterface.getIesOrderNumber()).isEqualTo(UPDATED_IES_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingDataSheetInterface() throws Exception {
        int databaseSizeBeforeUpdate = dataSheetInterfaceRepository.findAll().size();
        dataSheetInterface.setId(count.incrementAndGet());

        // Create the DataSheetInterface
        DataSheetInterfaceDTO dataSheetInterfaceDTO = dataSheetInterfaceMapper.toDto(dataSheetInterface);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataSheetInterfaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dataSheetInterfaceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataSheetInterfaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataSheetInterface in the database
        List<DataSheetInterface> dataSheetInterfaceList = dataSheetInterfaceRepository.findAll();
        assertThat(dataSheetInterfaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDataSheetInterface() throws Exception {
        int databaseSizeBeforeUpdate = dataSheetInterfaceRepository.findAll().size();
        dataSheetInterface.setId(count.incrementAndGet());

        // Create the DataSheetInterface
        DataSheetInterfaceDTO dataSheetInterfaceDTO = dataSheetInterfaceMapper.toDto(dataSheetInterface);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataSheetInterfaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataSheetInterfaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataSheetInterface in the database
        List<DataSheetInterface> dataSheetInterfaceList = dataSheetInterfaceRepository.findAll();
        assertThat(dataSheetInterfaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDataSheetInterface() throws Exception {
        int databaseSizeBeforeUpdate = dataSheetInterfaceRepository.findAll().size();
        dataSheetInterface.setId(count.incrementAndGet());

        // Create the DataSheetInterface
        DataSheetInterfaceDTO dataSheetInterfaceDTO = dataSheetInterfaceMapper.toDto(dataSheetInterface);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataSheetInterfaceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataSheetInterfaceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataSheetInterface in the database
        List<DataSheetInterface> dataSheetInterfaceList = dataSheetInterfaceRepository.findAll();
        assertThat(dataSheetInterfaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDataSheetInterfaceWithPatch() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        int databaseSizeBeforeUpdate = dataSheetInterfaceRepository.findAll().size();

        // Update the dataSheetInterface using partial update
        DataSheetInterface partialUpdatedDataSheetInterface = new DataSheetInterface();
        partialUpdatedDataSheetInterface.setId(dataSheetInterface.getId());

        partialUpdatedDataSheetInterface.amountReceived(UPDATED_AMOUNT_RECEIVED);

        restDataSheetInterfaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataSheetInterface.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataSheetInterface))
            )
            .andExpect(status().isOk());

        // Validate the DataSheetInterface in the database
        List<DataSheetInterface> dataSheetInterfaceList = dataSheetInterfaceRepository.findAll();
        assertThat(dataSheetInterfaceList).hasSize(databaseSizeBeforeUpdate);
        DataSheetInterface testDataSheetInterface = dataSheetInterfaceList.get(dataSheetInterfaceList.size() - 1);
        assertThat(testDataSheetInterface.getColcircuitosLotNumber()).isEqualTo(DEFAULT_COLCIRCUITOS_LOT_NUMBER);
        assertThat(testDataSheetInterface.getOrderAmount()).isEqualTo(DEFAULT_ORDER_AMOUNT);
        assertThat(testDataSheetInterface.getAmountReceived()).isEqualTo(UPDATED_AMOUNT_RECEIVED);
        assertThat(testDataSheetInterface.getRemission()).isEqualTo(DEFAULT_REMISSION);
        assertThat(testDataSheetInterface.getEntryDate()).isEqualTo(DEFAULT_ENTRY_DATE);
        assertThat(testDataSheetInterface.getIesOrderNumber()).isEqualTo(DEFAULT_IES_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateDataSheetInterfaceWithPatch() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        int databaseSizeBeforeUpdate = dataSheetInterfaceRepository.findAll().size();

        // Update the dataSheetInterface using partial update
        DataSheetInterface partialUpdatedDataSheetInterface = new DataSheetInterface();
        partialUpdatedDataSheetInterface.setId(dataSheetInterface.getId());

        partialUpdatedDataSheetInterface
            .colcircuitosLotNumber(UPDATED_COLCIRCUITOS_LOT_NUMBER)
            .orderAmount(UPDATED_ORDER_AMOUNT)
            .amountReceived(UPDATED_AMOUNT_RECEIVED)
            .remission(UPDATED_REMISSION)
            .entryDate(UPDATED_ENTRY_DATE)
            .iesOrderNumber(UPDATED_IES_ORDER_NUMBER);

        restDataSheetInterfaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataSheetInterface.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataSheetInterface))
            )
            .andExpect(status().isOk());

        // Validate the DataSheetInterface in the database
        List<DataSheetInterface> dataSheetInterfaceList = dataSheetInterfaceRepository.findAll();
        assertThat(dataSheetInterfaceList).hasSize(databaseSizeBeforeUpdate);
        DataSheetInterface testDataSheetInterface = dataSheetInterfaceList.get(dataSheetInterfaceList.size() - 1);
        assertThat(testDataSheetInterface.getColcircuitosLotNumber()).isEqualTo(UPDATED_COLCIRCUITOS_LOT_NUMBER);
        assertThat(testDataSheetInterface.getOrderAmount()).isEqualTo(UPDATED_ORDER_AMOUNT);
        assertThat(testDataSheetInterface.getAmountReceived()).isEqualTo(UPDATED_AMOUNT_RECEIVED);
        assertThat(testDataSheetInterface.getRemission()).isEqualTo(UPDATED_REMISSION);
        assertThat(testDataSheetInterface.getEntryDate()).isEqualTo(UPDATED_ENTRY_DATE);
        assertThat(testDataSheetInterface.getIesOrderNumber()).isEqualTo(UPDATED_IES_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingDataSheetInterface() throws Exception {
        int databaseSizeBeforeUpdate = dataSheetInterfaceRepository.findAll().size();
        dataSheetInterface.setId(count.incrementAndGet());

        // Create the DataSheetInterface
        DataSheetInterfaceDTO dataSheetInterfaceDTO = dataSheetInterfaceMapper.toDto(dataSheetInterface);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataSheetInterfaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dataSheetInterfaceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataSheetInterfaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataSheetInterface in the database
        List<DataSheetInterface> dataSheetInterfaceList = dataSheetInterfaceRepository.findAll();
        assertThat(dataSheetInterfaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDataSheetInterface() throws Exception {
        int databaseSizeBeforeUpdate = dataSheetInterfaceRepository.findAll().size();
        dataSheetInterface.setId(count.incrementAndGet());

        // Create the DataSheetInterface
        DataSheetInterfaceDTO dataSheetInterfaceDTO = dataSheetInterfaceMapper.toDto(dataSheetInterface);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataSheetInterfaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataSheetInterfaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataSheetInterface in the database
        List<DataSheetInterface> dataSheetInterfaceList = dataSheetInterfaceRepository.findAll();
        assertThat(dataSheetInterfaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDataSheetInterface() throws Exception {
        int databaseSizeBeforeUpdate = dataSheetInterfaceRepository.findAll().size();
        dataSheetInterface.setId(count.incrementAndGet());

        // Create the DataSheetInterface
        DataSheetInterfaceDTO dataSheetInterfaceDTO = dataSheetInterfaceMapper.toDto(dataSheetInterface);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataSheetInterfaceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataSheetInterfaceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataSheetInterface in the database
        List<DataSheetInterface> dataSheetInterfaceList = dataSheetInterfaceRepository.findAll();
        assertThat(dataSheetInterfaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDataSheetInterface() throws Exception {
        // Initialize the database
        dataSheetInterfaceRepository.saveAndFlush(dataSheetInterface);

        int databaseSizeBeforeDelete = dataSheetInterfaceRepository.findAll().size();

        // Delete the dataSheetInterface
        restDataSheetInterfaceMockMvc
            .perform(delete(ENTITY_API_URL_ID, dataSheetInterface.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DataSheetInterface> dataSheetInterfaceList = dataSheetInterfaceRepository.findAll();
        assertThat(dataSheetInterfaceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
