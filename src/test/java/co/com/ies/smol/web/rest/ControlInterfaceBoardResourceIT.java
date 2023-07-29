package co.com.ies.smol.web.rest;

import static co.com.ies.smol.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.Contract;
import co.com.ies.smol.domain.ControlInterfaceBoard;
import co.com.ies.smol.domain.InterfaceBoard;
import co.com.ies.smol.domain.enumeration.Location;
import co.com.ies.smol.domain.enumeration.StatusInterfaceBoard;
import co.com.ies.smol.repository.ControlInterfaceBoardRepository;
import co.com.ies.smol.service.ControlInterfaceBoardService;
import co.com.ies.smol.service.criteria.ControlInterfaceBoardCriteria;
import co.com.ies.smol.service.dto.ControlInterfaceBoardDTO;
import co.com.ies.smol.service.mapper.ControlInterfaceBoardMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ControlInterfaceBoardResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ControlInterfaceBoardResourceIT {

    private static final Location DEFAULT_LOCATION = Location.IES;
    private static final Location UPDATED_LOCATION = Location.CLIENT;

    private static final StatusInterfaceBoard DEFAULT_STATE = StatusInterfaceBoard.OPERATION;
    private static final StatusInterfaceBoard UPDATED_STATE = StatusInterfaceBoard.STOCK;

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_FINISH_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FINISH_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_FINISH_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/control-interface-boards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ControlInterfaceBoardRepository controlInterfaceBoardRepository;

    @Mock
    private ControlInterfaceBoardRepository controlInterfaceBoardRepositoryMock;

    @Autowired
    private ControlInterfaceBoardMapper controlInterfaceBoardMapper;

    @Mock
    private ControlInterfaceBoardService controlInterfaceBoardServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restControlInterfaceBoardMockMvc;

    private ControlInterfaceBoard controlInterfaceBoard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ControlInterfaceBoard createEntity(EntityManager em) {
        ControlInterfaceBoard controlInterfaceBoard = new ControlInterfaceBoard()
            .location(DEFAULT_LOCATION)
            .state(DEFAULT_STATE)
            .startTime(DEFAULT_START_TIME)
            .finishTime(DEFAULT_FINISH_TIME);
        // Add required entity
        InterfaceBoard interfaceBoard;
        if (TestUtil.findAll(em, InterfaceBoard.class).isEmpty()) {
            interfaceBoard = InterfaceBoardResourceIT.createEntity(em);
            em.persist(interfaceBoard);
            em.flush();
        } else {
            interfaceBoard = TestUtil.findAll(em, InterfaceBoard.class).get(0);
        }
        controlInterfaceBoard.setInterfaceBoard(interfaceBoard);
        return controlInterfaceBoard;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ControlInterfaceBoard createUpdatedEntity(EntityManager em) {
        ControlInterfaceBoard controlInterfaceBoard = new ControlInterfaceBoard()
            .location(UPDATED_LOCATION)
            .state(UPDATED_STATE)
            .startTime(UPDATED_START_TIME)
            .finishTime(UPDATED_FINISH_TIME);
        // Add required entity
        InterfaceBoard interfaceBoard;
        if (TestUtil.findAll(em, InterfaceBoard.class).isEmpty()) {
            interfaceBoard = InterfaceBoardResourceIT.createUpdatedEntity(em);
            em.persist(interfaceBoard);
            em.flush();
        } else {
            interfaceBoard = TestUtil.findAll(em, InterfaceBoard.class).get(0);
        }
        controlInterfaceBoard.setInterfaceBoard(interfaceBoard);
        return controlInterfaceBoard;
    }

    @BeforeEach
    public void initTest() {
        controlInterfaceBoard = createEntity(em);
    }

    @Test
    @Transactional
    void createControlInterfaceBoard() throws Exception {
        int databaseSizeBeforeCreate = controlInterfaceBoardRepository.findAll().size();
        // Create the ControlInterfaceBoard
        ControlInterfaceBoardDTO controlInterfaceBoardDTO = controlInterfaceBoardMapper.toDto(controlInterfaceBoard);
        restControlInterfaceBoardMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(controlInterfaceBoardDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ControlInterfaceBoard in the database
        List<ControlInterfaceBoard> controlInterfaceBoardList = controlInterfaceBoardRepository.findAll();
        assertThat(controlInterfaceBoardList).hasSize(databaseSizeBeforeCreate + 1);
        ControlInterfaceBoard testControlInterfaceBoard = controlInterfaceBoardList.get(controlInterfaceBoardList.size() - 1);
        assertThat(testControlInterfaceBoard.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testControlInterfaceBoard.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testControlInterfaceBoard.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testControlInterfaceBoard.getFinishTime()).isEqualTo(DEFAULT_FINISH_TIME);
    }

    @Test
    @Transactional
    void createControlInterfaceBoardWithExistingId() throws Exception {
        // Create the ControlInterfaceBoard with an existing ID
        controlInterfaceBoard.setId(1L);
        ControlInterfaceBoardDTO controlInterfaceBoardDTO = controlInterfaceBoardMapper.toDto(controlInterfaceBoard);

        int databaseSizeBeforeCreate = controlInterfaceBoardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restControlInterfaceBoardMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(controlInterfaceBoardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ControlInterfaceBoard in the database
        List<ControlInterfaceBoard> controlInterfaceBoardList = controlInterfaceBoardRepository.findAll();
        assertThat(controlInterfaceBoardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = controlInterfaceBoardRepository.findAll().size();
        // set the field null
        controlInterfaceBoard.setLocation(null);

        // Create the ControlInterfaceBoard, which fails.
        ControlInterfaceBoardDTO controlInterfaceBoardDTO = controlInterfaceBoardMapper.toDto(controlInterfaceBoard);

        restControlInterfaceBoardMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(controlInterfaceBoardDTO))
            )
            .andExpect(status().isBadRequest());

        List<ControlInterfaceBoard> controlInterfaceBoardList = controlInterfaceBoardRepository.findAll();
        assertThat(controlInterfaceBoardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = controlInterfaceBoardRepository.findAll().size();
        // set the field null
        controlInterfaceBoard.setState(null);

        // Create the ControlInterfaceBoard, which fails.
        ControlInterfaceBoardDTO controlInterfaceBoardDTO = controlInterfaceBoardMapper.toDto(controlInterfaceBoard);

        restControlInterfaceBoardMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(controlInterfaceBoardDTO))
            )
            .andExpect(status().isBadRequest());

        List<ControlInterfaceBoard> controlInterfaceBoardList = controlInterfaceBoardRepository.findAll();
        assertThat(controlInterfaceBoardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = controlInterfaceBoardRepository.findAll().size();
        // set the field null
        controlInterfaceBoard.setStartTime(null);

        // Create the ControlInterfaceBoard, which fails.
        ControlInterfaceBoardDTO controlInterfaceBoardDTO = controlInterfaceBoardMapper.toDto(controlInterfaceBoard);

        restControlInterfaceBoardMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(controlInterfaceBoardDTO))
            )
            .andExpect(status().isBadRequest());

        List<ControlInterfaceBoard> controlInterfaceBoardList = controlInterfaceBoardRepository.findAll();
        assertThat(controlInterfaceBoardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoards() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get all the controlInterfaceBoardList
        restControlInterfaceBoardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(controlInterfaceBoard.getId().intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].finishTime").value(hasItem(sameInstant(DEFAULT_FINISH_TIME))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllControlInterfaceBoardsWithEagerRelationshipsIsEnabled() throws Exception {
        when(controlInterfaceBoardServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restControlInterfaceBoardMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(controlInterfaceBoardServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllControlInterfaceBoardsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(controlInterfaceBoardServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restControlInterfaceBoardMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(controlInterfaceBoardRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getControlInterfaceBoard() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get the controlInterfaceBoard
        restControlInterfaceBoardMockMvc
            .perform(get(ENTITY_API_URL_ID, controlInterfaceBoard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(controlInterfaceBoard.getId().intValue()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.startTime").value(sameInstant(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.finishTime").value(sameInstant(DEFAULT_FINISH_TIME)));
    }

    @Test
    @Transactional
    void getControlInterfaceBoardsByIdFiltering() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        Long id = controlInterfaceBoard.getId();

        defaultControlInterfaceBoardShouldBeFound("id.equals=" + id);
        defaultControlInterfaceBoardShouldNotBeFound("id.notEquals=" + id);

        defaultControlInterfaceBoardShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultControlInterfaceBoardShouldNotBeFound("id.greaterThan=" + id);

        defaultControlInterfaceBoardShouldBeFound("id.lessThanOrEqual=" + id);
        defaultControlInterfaceBoardShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get all the controlInterfaceBoardList where location equals to DEFAULT_LOCATION
        defaultControlInterfaceBoardShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the controlInterfaceBoardList where location equals to UPDATED_LOCATION
        defaultControlInterfaceBoardShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get all the controlInterfaceBoardList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultControlInterfaceBoardShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the controlInterfaceBoardList where location equals to UPDATED_LOCATION
        defaultControlInterfaceBoardShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get all the controlInterfaceBoardList where location is not null
        defaultControlInterfaceBoardShouldBeFound("location.specified=true");

        // Get all the controlInterfaceBoardList where location is null
        defaultControlInterfaceBoardShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get all the controlInterfaceBoardList where state equals to DEFAULT_STATE
        defaultControlInterfaceBoardShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the controlInterfaceBoardList where state equals to UPDATED_STATE
        defaultControlInterfaceBoardShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get all the controlInterfaceBoardList where state in DEFAULT_STATE or UPDATED_STATE
        defaultControlInterfaceBoardShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the controlInterfaceBoardList where state equals to UPDATED_STATE
        defaultControlInterfaceBoardShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get all the controlInterfaceBoardList where state is not null
        defaultControlInterfaceBoardShouldBeFound("state.specified=true");

        // Get all the controlInterfaceBoardList where state is null
        defaultControlInterfaceBoardShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get all the controlInterfaceBoardList where startTime equals to DEFAULT_START_TIME
        defaultControlInterfaceBoardShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the controlInterfaceBoardList where startTime equals to UPDATED_START_TIME
        defaultControlInterfaceBoardShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get all the controlInterfaceBoardList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultControlInterfaceBoardShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the controlInterfaceBoardList where startTime equals to UPDATED_START_TIME
        defaultControlInterfaceBoardShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get all the controlInterfaceBoardList where startTime is not null
        defaultControlInterfaceBoardShouldBeFound("startTime.specified=true");

        // Get all the controlInterfaceBoardList where startTime is null
        defaultControlInterfaceBoardShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByStartTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get all the controlInterfaceBoardList where startTime is greater than or equal to DEFAULT_START_TIME
        defaultControlInterfaceBoardShouldBeFound("startTime.greaterThanOrEqual=" + DEFAULT_START_TIME);

        // Get all the controlInterfaceBoardList where startTime is greater than or equal to UPDATED_START_TIME
        defaultControlInterfaceBoardShouldNotBeFound("startTime.greaterThanOrEqual=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByStartTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get all the controlInterfaceBoardList where startTime is less than or equal to DEFAULT_START_TIME
        defaultControlInterfaceBoardShouldBeFound("startTime.lessThanOrEqual=" + DEFAULT_START_TIME);

        // Get all the controlInterfaceBoardList where startTime is less than or equal to SMALLER_START_TIME
        defaultControlInterfaceBoardShouldNotBeFound("startTime.lessThanOrEqual=" + SMALLER_START_TIME);
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByStartTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get all the controlInterfaceBoardList where startTime is less than DEFAULT_START_TIME
        defaultControlInterfaceBoardShouldNotBeFound("startTime.lessThan=" + DEFAULT_START_TIME);

        // Get all the controlInterfaceBoardList where startTime is less than UPDATED_START_TIME
        defaultControlInterfaceBoardShouldBeFound("startTime.lessThan=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByStartTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get all the controlInterfaceBoardList where startTime is greater than DEFAULT_START_TIME
        defaultControlInterfaceBoardShouldNotBeFound("startTime.greaterThan=" + DEFAULT_START_TIME);

        // Get all the controlInterfaceBoardList where startTime is greater than SMALLER_START_TIME
        defaultControlInterfaceBoardShouldBeFound("startTime.greaterThan=" + SMALLER_START_TIME);
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByFinishTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get all the controlInterfaceBoardList where finishTime equals to DEFAULT_FINISH_TIME
        defaultControlInterfaceBoardShouldBeFound("finishTime.equals=" + DEFAULT_FINISH_TIME);

        // Get all the controlInterfaceBoardList where finishTime equals to UPDATED_FINISH_TIME
        defaultControlInterfaceBoardShouldNotBeFound("finishTime.equals=" + UPDATED_FINISH_TIME);
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByFinishTimeIsInShouldWork() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get all the controlInterfaceBoardList where finishTime in DEFAULT_FINISH_TIME or UPDATED_FINISH_TIME
        defaultControlInterfaceBoardShouldBeFound("finishTime.in=" + DEFAULT_FINISH_TIME + "," + UPDATED_FINISH_TIME);

        // Get all the controlInterfaceBoardList where finishTime equals to UPDATED_FINISH_TIME
        defaultControlInterfaceBoardShouldNotBeFound("finishTime.in=" + UPDATED_FINISH_TIME);
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByFinishTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get all the controlInterfaceBoardList where finishTime is not null
        defaultControlInterfaceBoardShouldBeFound("finishTime.specified=true");

        // Get all the controlInterfaceBoardList where finishTime is null
        defaultControlInterfaceBoardShouldNotBeFound("finishTime.specified=false");
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByFinishTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get all the controlInterfaceBoardList where finishTime is greater than or equal to DEFAULT_FINISH_TIME
        defaultControlInterfaceBoardShouldBeFound("finishTime.greaterThanOrEqual=" + DEFAULT_FINISH_TIME);

        // Get all the controlInterfaceBoardList where finishTime is greater than or equal to UPDATED_FINISH_TIME
        defaultControlInterfaceBoardShouldNotBeFound("finishTime.greaterThanOrEqual=" + UPDATED_FINISH_TIME);
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByFinishTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get all the controlInterfaceBoardList where finishTime is less than or equal to DEFAULT_FINISH_TIME
        defaultControlInterfaceBoardShouldBeFound("finishTime.lessThanOrEqual=" + DEFAULT_FINISH_TIME);

        // Get all the controlInterfaceBoardList where finishTime is less than or equal to SMALLER_FINISH_TIME
        defaultControlInterfaceBoardShouldNotBeFound("finishTime.lessThanOrEqual=" + SMALLER_FINISH_TIME);
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByFinishTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get all the controlInterfaceBoardList where finishTime is less than DEFAULT_FINISH_TIME
        defaultControlInterfaceBoardShouldNotBeFound("finishTime.lessThan=" + DEFAULT_FINISH_TIME);

        // Get all the controlInterfaceBoardList where finishTime is less than UPDATED_FINISH_TIME
        defaultControlInterfaceBoardShouldBeFound("finishTime.lessThan=" + UPDATED_FINISH_TIME);
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByFinishTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        // Get all the controlInterfaceBoardList where finishTime is greater than DEFAULT_FINISH_TIME
        defaultControlInterfaceBoardShouldNotBeFound("finishTime.greaterThan=" + DEFAULT_FINISH_TIME);

        // Get all the controlInterfaceBoardList where finishTime is greater than SMALLER_FINISH_TIME
        defaultControlInterfaceBoardShouldBeFound("finishTime.greaterThan=" + SMALLER_FINISH_TIME);
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByContractIsEqualToSomething() throws Exception {
        Contract contract;
        if (TestUtil.findAll(em, Contract.class).isEmpty()) {
            controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);
            contract = ContractResourceIT.createEntity(em);
        } else {
            contract = TestUtil.findAll(em, Contract.class).get(0);
        }
        em.persist(contract);
        em.flush();
        controlInterfaceBoard.setContract(contract);
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);
        Long contractId = contract.getId();

        // Get all the controlInterfaceBoardList where contract equals to contractId
        defaultControlInterfaceBoardShouldBeFound("contractId.equals=" + contractId);

        // Get all the controlInterfaceBoardList where contract equals to (contractId + 1)
        defaultControlInterfaceBoardShouldNotBeFound("contractId.equals=" + (contractId + 1));
    }

    @Test
    @Transactional
    void getAllControlInterfaceBoardsByInterfaceBoardIsEqualToSomething() throws Exception {
        InterfaceBoard interfaceBoard;
        if (TestUtil.findAll(em, InterfaceBoard.class).isEmpty()) {
            controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);
            interfaceBoard = InterfaceBoardResourceIT.createEntity(em);
        } else {
            interfaceBoard = TestUtil.findAll(em, InterfaceBoard.class).get(0);
        }
        em.persist(interfaceBoard);
        em.flush();
        controlInterfaceBoard.setInterfaceBoard(interfaceBoard);
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);
        Long interfaceBoardId = interfaceBoard.getId();

        // Get all the controlInterfaceBoardList where interfaceBoard equals to interfaceBoardId
        defaultControlInterfaceBoardShouldBeFound("interfaceBoardId.equals=" + interfaceBoardId);

        // Get all the controlInterfaceBoardList where interfaceBoard equals to (interfaceBoardId + 1)
        defaultControlInterfaceBoardShouldNotBeFound("interfaceBoardId.equals=" + (interfaceBoardId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultControlInterfaceBoardShouldBeFound(String filter) throws Exception {
        restControlInterfaceBoardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(controlInterfaceBoard.getId().intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].finishTime").value(hasItem(sameInstant(DEFAULT_FINISH_TIME))));

        // Check, that the count call also returns 1
        restControlInterfaceBoardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultControlInterfaceBoardShouldNotBeFound(String filter) throws Exception {
        restControlInterfaceBoardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restControlInterfaceBoardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingControlInterfaceBoard() throws Exception {
        // Get the controlInterfaceBoard
        restControlInterfaceBoardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingControlInterfaceBoard() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        int databaseSizeBeforeUpdate = controlInterfaceBoardRepository.findAll().size();

        // Update the controlInterfaceBoard
        ControlInterfaceBoard updatedControlInterfaceBoard = controlInterfaceBoardRepository.findById(controlInterfaceBoard.getId()).get();
        // Disconnect from session so that the updates on updatedControlInterfaceBoard are not directly saved in db
        em.detach(updatedControlInterfaceBoard);
        updatedControlInterfaceBoard
            .location(UPDATED_LOCATION)
            .state(UPDATED_STATE)
            .startTime(UPDATED_START_TIME)
            .finishTime(UPDATED_FINISH_TIME);
        ControlInterfaceBoardDTO controlInterfaceBoardDTO = controlInterfaceBoardMapper.toDto(updatedControlInterfaceBoard);

        restControlInterfaceBoardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, controlInterfaceBoardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(controlInterfaceBoardDTO))
            )
            .andExpect(status().isOk());

        // Validate the ControlInterfaceBoard in the database
        List<ControlInterfaceBoard> controlInterfaceBoardList = controlInterfaceBoardRepository.findAll();
        assertThat(controlInterfaceBoardList).hasSize(databaseSizeBeforeUpdate);
        ControlInterfaceBoard testControlInterfaceBoard = controlInterfaceBoardList.get(controlInterfaceBoardList.size() - 1);
        assertThat(testControlInterfaceBoard.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testControlInterfaceBoard.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testControlInterfaceBoard.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testControlInterfaceBoard.getFinishTime()).isEqualTo(UPDATED_FINISH_TIME);
    }

    @Test
    @Transactional
    void putNonExistingControlInterfaceBoard() throws Exception {
        int databaseSizeBeforeUpdate = controlInterfaceBoardRepository.findAll().size();
        controlInterfaceBoard.setId(count.incrementAndGet());

        // Create the ControlInterfaceBoard
        ControlInterfaceBoardDTO controlInterfaceBoardDTO = controlInterfaceBoardMapper.toDto(controlInterfaceBoard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restControlInterfaceBoardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, controlInterfaceBoardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(controlInterfaceBoardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ControlInterfaceBoard in the database
        List<ControlInterfaceBoard> controlInterfaceBoardList = controlInterfaceBoardRepository.findAll();
        assertThat(controlInterfaceBoardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchControlInterfaceBoard() throws Exception {
        int databaseSizeBeforeUpdate = controlInterfaceBoardRepository.findAll().size();
        controlInterfaceBoard.setId(count.incrementAndGet());

        // Create the ControlInterfaceBoard
        ControlInterfaceBoardDTO controlInterfaceBoardDTO = controlInterfaceBoardMapper.toDto(controlInterfaceBoard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restControlInterfaceBoardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(controlInterfaceBoardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ControlInterfaceBoard in the database
        List<ControlInterfaceBoard> controlInterfaceBoardList = controlInterfaceBoardRepository.findAll();
        assertThat(controlInterfaceBoardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamControlInterfaceBoard() throws Exception {
        int databaseSizeBeforeUpdate = controlInterfaceBoardRepository.findAll().size();
        controlInterfaceBoard.setId(count.incrementAndGet());

        // Create the ControlInterfaceBoard
        ControlInterfaceBoardDTO controlInterfaceBoardDTO = controlInterfaceBoardMapper.toDto(controlInterfaceBoard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restControlInterfaceBoardMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(controlInterfaceBoardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ControlInterfaceBoard in the database
        List<ControlInterfaceBoard> controlInterfaceBoardList = controlInterfaceBoardRepository.findAll();
        assertThat(controlInterfaceBoardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateControlInterfaceBoardWithPatch() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        int databaseSizeBeforeUpdate = controlInterfaceBoardRepository.findAll().size();

        // Update the controlInterfaceBoard using partial update
        ControlInterfaceBoard partialUpdatedControlInterfaceBoard = new ControlInterfaceBoard();
        partialUpdatedControlInterfaceBoard.setId(controlInterfaceBoard.getId());

        partialUpdatedControlInterfaceBoard.state(UPDATED_STATE).finishTime(UPDATED_FINISH_TIME);

        restControlInterfaceBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedControlInterfaceBoard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedControlInterfaceBoard))
            )
            .andExpect(status().isOk());

        // Validate the ControlInterfaceBoard in the database
        List<ControlInterfaceBoard> controlInterfaceBoardList = controlInterfaceBoardRepository.findAll();
        assertThat(controlInterfaceBoardList).hasSize(databaseSizeBeforeUpdate);
        ControlInterfaceBoard testControlInterfaceBoard = controlInterfaceBoardList.get(controlInterfaceBoardList.size() - 1);
        assertThat(testControlInterfaceBoard.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testControlInterfaceBoard.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testControlInterfaceBoard.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testControlInterfaceBoard.getFinishTime()).isEqualTo(UPDATED_FINISH_TIME);
    }

    @Test
    @Transactional
    void fullUpdateControlInterfaceBoardWithPatch() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        int databaseSizeBeforeUpdate = controlInterfaceBoardRepository.findAll().size();

        // Update the controlInterfaceBoard using partial update
        ControlInterfaceBoard partialUpdatedControlInterfaceBoard = new ControlInterfaceBoard();
        partialUpdatedControlInterfaceBoard.setId(controlInterfaceBoard.getId());

        partialUpdatedControlInterfaceBoard
            .location(UPDATED_LOCATION)
            .state(UPDATED_STATE)
            .startTime(UPDATED_START_TIME)
            .finishTime(UPDATED_FINISH_TIME);

        restControlInterfaceBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedControlInterfaceBoard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedControlInterfaceBoard))
            )
            .andExpect(status().isOk());

        // Validate the ControlInterfaceBoard in the database
        List<ControlInterfaceBoard> controlInterfaceBoardList = controlInterfaceBoardRepository.findAll();
        assertThat(controlInterfaceBoardList).hasSize(databaseSizeBeforeUpdate);
        ControlInterfaceBoard testControlInterfaceBoard = controlInterfaceBoardList.get(controlInterfaceBoardList.size() - 1);
        assertThat(testControlInterfaceBoard.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testControlInterfaceBoard.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testControlInterfaceBoard.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testControlInterfaceBoard.getFinishTime()).isEqualTo(UPDATED_FINISH_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingControlInterfaceBoard() throws Exception {
        int databaseSizeBeforeUpdate = controlInterfaceBoardRepository.findAll().size();
        controlInterfaceBoard.setId(count.incrementAndGet());

        // Create the ControlInterfaceBoard
        ControlInterfaceBoardDTO controlInterfaceBoardDTO = controlInterfaceBoardMapper.toDto(controlInterfaceBoard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restControlInterfaceBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, controlInterfaceBoardDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(controlInterfaceBoardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ControlInterfaceBoard in the database
        List<ControlInterfaceBoard> controlInterfaceBoardList = controlInterfaceBoardRepository.findAll();
        assertThat(controlInterfaceBoardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchControlInterfaceBoard() throws Exception {
        int databaseSizeBeforeUpdate = controlInterfaceBoardRepository.findAll().size();
        controlInterfaceBoard.setId(count.incrementAndGet());

        // Create the ControlInterfaceBoard
        ControlInterfaceBoardDTO controlInterfaceBoardDTO = controlInterfaceBoardMapper.toDto(controlInterfaceBoard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restControlInterfaceBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(controlInterfaceBoardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ControlInterfaceBoard in the database
        List<ControlInterfaceBoard> controlInterfaceBoardList = controlInterfaceBoardRepository.findAll();
        assertThat(controlInterfaceBoardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamControlInterfaceBoard() throws Exception {
        int databaseSizeBeforeUpdate = controlInterfaceBoardRepository.findAll().size();
        controlInterfaceBoard.setId(count.incrementAndGet());

        // Create the ControlInterfaceBoard
        ControlInterfaceBoardDTO controlInterfaceBoardDTO = controlInterfaceBoardMapper.toDto(controlInterfaceBoard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restControlInterfaceBoardMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(controlInterfaceBoardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ControlInterfaceBoard in the database
        List<ControlInterfaceBoard> controlInterfaceBoardList = controlInterfaceBoardRepository.findAll();
        assertThat(controlInterfaceBoardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteControlInterfaceBoard() throws Exception {
        // Initialize the database
        controlInterfaceBoardRepository.saveAndFlush(controlInterfaceBoard);

        int databaseSizeBeforeDelete = controlInterfaceBoardRepository.findAll().size();

        // Delete the controlInterfaceBoard
        restControlInterfaceBoardMockMvc
            .perform(delete(ENTITY_API_URL_ID, controlInterfaceBoard.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ControlInterfaceBoard> controlInterfaceBoardList = controlInterfaceBoardRepository.findAll();
        assertThat(controlInterfaceBoardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
