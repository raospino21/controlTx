package co.com.ies.smol.web.rest;

import static co.com.ies.smol.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.Contract;
import co.com.ies.smol.domain.Operator;
import co.com.ies.smol.domain.enumeration.ContractType;
import co.com.ies.smol.repository.ContractRepository;
import co.com.ies.smol.service.ContractService;
import co.com.ies.smol.service.criteria.ContractCriteria;
import co.com.ies.smol.service.dto.ContractDTO;
import co.com.ies.smol.service.mapper.ContractMapper;
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
 * Integration tests for the {@link ContractResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContractResourceIT {

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final ContractType DEFAULT_TYPE = ContractType.SALE;
    private static final ContractType UPDATED_TYPE = ContractType.RENT;

    private static final Long DEFAULT_NUMBER_INTERFACE_BOARD = 1L;
    private static final Long UPDATED_NUMBER_INTERFACE_BOARD = 2L;
    private static final Long SMALLER_NUMBER_INTERFACE_BOARD = 1L - 1L;

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_FINISH_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FINISH_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_FINISH_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContractRepository contractRepository;

    @Mock
    private ContractRepository contractRepositoryMock;

    @Autowired
    private ContractMapper contractMapper;

    @Mock
    private ContractService contractServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractMockMvc;

    private Contract contract;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createEntity(EntityManager em) {
        Contract contract = new Contract()
            .reference(DEFAULT_REFERENCE)
            .type(DEFAULT_TYPE)
            .numberInterfaceBoard(DEFAULT_NUMBER_INTERFACE_BOARD)
            .startTime(DEFAULT_START_TIME)
            .finishTime(DEFAULT_FINISH_TIME);
        // Add required entity
        Operator operator;
        if (TestUtil.findAll(em, Operator.class).isEmpty()) {
            operator = OperatorResourceIT.createEntity(em);
            em.persist(operator);
            em.flush();
        } else {
            operator = TestUtil.findAll(em, Operator.class).get(0);
        }
        contract.setOperator(operator);
        return contract;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createUpdatedEntity(EntityManager em) {
        Contract contract = new Contract()
            .reference(UPDATED_REFERENCE)
            .type(UPDATED_TYPE)
            .numberInterfaceBoard(UPDATED_NUMBER_INTERFACE_BOARD)
            .startTime(UPDATED_START_TIME)
            .finishTime(UPDATED_FINISH_TIME);
        // Add required entity
        Operator operator;
        if (TestUtil.findAll(em, Operator.class).isEmpty()) {
            operator = OperatorResourceIT.createUpdatedEntity(em);
            em.persist(operator);
            em.flush();
        } else {
            operator = TestUtil.findAll(em, Operator.class).get(0);
        }
        contract.setOperator(operator);
        return contract;
    }

    @BeforeEach
    public void initTest() {
        contract = createEntity(em);
    }

    @Test
    @Transactional
    void createContract() throws Exception {
        int databaseSizeBeforeCreate = contractRepository.findAll().size();
        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);
        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isCreated());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate + 1);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testContract.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testContract.getNumberInterfaceBoard()).isEqualTo(DEFAULT_NUMBER_INTERFACE_BOARD);
        assertThat(testContract.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testContract.getFinishTime()).isEqualTo(DEFAULT_FINISH_TIME);
    }

    @Test
    @Transactional
    void createContractWithExistingId() throws Exception {
        // Create the Contract with an existing ID
        contract.setId(1L);
        ContractDTO contractDTO = contractMapper.toDto(contract);

        int databaseSizeBeforeCreate = contractRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setType(null);

        // Create the Contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumberInterfaceBoardIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setNumberInterfaceBoard(null);

        // Create the Contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setStartTime(null);

        // Create the Contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContracts() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].numberInterfaceBoard").value(hasItem(DEFAULT_NUMBER_INTERFACE_BOARD.intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].finishTime").value(hasItem(sameInstant(DEFAULT_FINISH_TIME))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContractsWithEagerRelationshipsIsEnabled() throws Exception {
        when(contractServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContractMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(contractServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContractsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(contractServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContractMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(contractRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get the contract
        restContractMockMvc
            .perform(get(ENTITY_API_URL_ID, contract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contract.getId().intValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.numberInterfaceBoard").value(DEFAULT_NUMBER_INTERFACE_BOARD.intValue()))
            .andExpect(jsonPath("$.startTime").value(sameInstant(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.finishTime").value(sameInstant(DEFAULT_FINISH_TIME)));
    }

    @Test
    @Transactional
    void getContractsByIdFiltering() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        Long id = contract.getId();

        defaultContractShouldBeFound("id.equals=" + id);
        defaultContractShouldNotBeFound("id.notEquals=" + id);

        defaultContractShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContractShouldNotBeFound("id.greaterThan=" + id);

        defaultContractShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContractShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContractsByReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where reference equals to DEFAULT_REFERENCE
        defaultContractShouldBeFound("reference.equals=" + DEFAULT_REFERENCE);

        // Get all the contractList where reference equals to UPDATED_REFERENCE
        defaultContractShouldNotBeFound("reference.equals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllContractsByReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where reference in DEFAULT_REFERENCE or UPDATED_REFERENCE
        defaultContractShouldBeFound("reference.in=" + DEFAULT_REFERENCE + "," + UPDATED_REFERENCE);

        // Get all the contractList where reference equals to UPDATED_REFERENCE
        defaultContractShouldNotBeFound("reference.in=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllContractsByReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where reference is not null
        defaultContractShouldBeFound("reference.specified=true");

        // Get all the contractList where reference is null
        defaultContractShouldNotBeFound("reference.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByReferenceContainsSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where reference contains DEFAULT_REFERENCE
        defaultContractShouldBeFound("reference.contains=" + DEFAULT_REFERENCE);

        // Get all the contractList where reference contains UPDATED_REFERENCE
        defaultContractShouldNotBeFound("reference.contains=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllContractsByReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where reference does not contain DEFAULT_REFERENCE
        defaultContractShouldNotBeFound("reference.doesNotContain=" + DEFAULT_REFERENCE);

        // Get all the contractList where reference does not contain UPDATED_REFERENCE
        defaultContractShouldBeFound("reference.doesNotContain=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllContractsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where type equals to DEFAULT_TYPE
        defaultContractShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the contractList where type equals to UPDATED_TYPE
        defaultContractShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllContractsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultContractShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the contractList where type equals to UPDATED_TYPE
        defaultContractShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllContractsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where type is not null
        defaultContractShouldBeFound("type.specified=true");

        // Get all the contractList where type is null
        defaultContractShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByNumberInterfaceBoardIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where numberInterfaceBoard equals to DEFAULT_NUMBER_INTERFACE_BOARD
        defaultContractShouldBeFound("numberInterfaceBoard.equals=" + DEFAULT_NUMBER_INTERFACE_BOARD);

        // Get all the contractList where numberInterfaceBoard equals to UPDATED_NUMBER_INTERFACE_BOARD
        defaultContractShouldNotBeFound("numberInterfaceBoard.equals=" + UPDATED_NUMBER_INTERFACE_BOARD);
    }

    @Test
    @Transactional
    void getAllContractsByNumberInterfaceBoardIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where numberInterfaceBoard in DEFAULT_NUMBER_INTERFACE_BOARD or UPDATED_NUMBER_INTERFACE_BOARD
        defaultContractShouldBeFound("numberInterfaceBoard.in=" + DEFAULT_NUMBER_INTERFACE_BOARD + "," + UPDATED_NUMBER_INTERFACE_BOARD);

        // Get all the contractList where numberInterfaceBoard equals to UPDATED_NUMBER_INTERFACE_BOARD
        defaultContractShouldNotBeFound("numberInterfaceBoard.in=" + UPDATED_NUMBER_INTERFACE_BOARD);
    }

    @Test
    @Transactional
    void getAllContractsByNumberInterfaceBoardIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where numberInterfaceBoard is not null
        defaultContractShouldBeFound("numberInterfaceBoard.specified=true");

        // Get all the contractList where numberInterfaceBoard is null
        defaultContractShouldNotBeFound("numberInterfaceBoard.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByNumberInterfaceBoardIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where numberInterfaceBoard is greater than or equal to DEFAULT_NUMBER_INTERFACE_BOARD
        defaultContractShouldBeFound("numberInterfaceBoard.greaterThanOrEqual=" + DEFAULT_NUMBER_INTERFACE_BOARD);

        // Get all the contractList where numberInterfaceBoard is greater than or equal to UPDATED_NUMBER_INTERFACE_BOARD
        defaultContractShouldNotBeFound("numberInterfaceBoard.greaterThanOrEqual=" + UPDATED_NUMBER_INTERFACE_BOARD);
    }

    @Test
    @Transactional
    void getAllContractsByNumberInterfaceBoardIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where numberInterfaceBoard is less than or equal to DEFAULT_NUMBER_INTERFACE_BOARD
        defaultContractShouldBeFound("numberInterfaceBoard.lessThanOrEqual=" + DEFAULT_NUMBER_INTERFACE_BOARD);

        // Get all the contractList where numberInterfaceBoard is less than or equal to SMALLER_NUMBER_INTERFACE_BOARD
        defaultContractShouldNotBeFound("numberInterfaceBoard.lessThanOrEqual=" + SMALLER_NUMBER_INTERFACE_BOARD);
    }

    @Test
    @Transactional
    void getAllContractsByNumberInterfaceBoardIsLessThanSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where numberInterfaceBoard is less than DEFAULT_NUMBER_INTERFACE_BOARD
        defaultContractShouldNotBeFound("numberInterfaceBoard.lessThan=" + DEFAULT_NUMBER_INTERFACE_BOARD);

        // Get all the contractList where numberInterfaceBoard is less than UPDATED_NUMBER_INTERFACE_BOARD
        defaultContractShouldBeFound("numberInterfaceBoard.lessThan=" + UPDATED_NUMBER_INTERFACE_BOARD);
    }

    @Test
    @Transactional
    void getAllContractsByNumberInterfaceBoardIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where numberInterfaceBoard is greater than DEFAULT_NUMBER_INTERFACE_BOARD
        defaultContractShouldNotBeFound("numberInterfaceBoard.greaterThan=" + DEFAULT_NUMBER_INTERFACE_BOARD);

        // Get all the contractList where numberInterfaceBoard is greater than SMALLER_NUMBER_INTERFACE_BOARD
        defaultContractShouldBeFound("numberInterfaceBoard.greaterThan=" + SMALLER_NUMBER_INTERFACE_BOARD);
    }

    @Test
    @Transactional
    void getAllContractsByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where startTime equals to DEFAULT_START_TIME
        defaultContractShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the contractList where startTime equals to UPDATED_START_TIME
        defaultContractShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllContractsByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultContractShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the contractList where startTime equals to UPDATED_START_TIME
        defaultContractShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllContractsByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where startTime is not null
        defaultContractShouldBeFound("startTime.specified=true");

        // Get all the contractList where startTime is null
        defaultContractShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByStartTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where startTime is greater than or equal to DEFAULT_START_TIME
        defaultContractShouldBeFound("startTime.greaterThanOrEqual=" + DEFAULT_START_TIME);

        // Get all the contractList where startTime is greater than or equal to UPDATED_START_TIME
        defaultContractShouldNotBeFound("startTime.greaterThanOrEqual=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllContractsByStartTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where startTime is less than or equal to DEFAULT_START_TIME
        defaultContractShouldBeFound("startTime.lessThanOrEqual=" + DEFAULT_START_TIME);

        // Get all the contractList where startTime is less than or equal to SMALLER_START_TIME
        defaultContractShouldNotBeFound("startTime.lessThanOrEqual=" + SMALLER_START_TIME);
    }

    @Test
    @Transactional
    void getAllContractsByStartTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where startTime is less than DEFAULT_START_TIME
        defaultContractShouldNotBeFound("startTime.lessThan=" + DEFAULT_START_TIME);

        // Get all the contractList where startTime is less than UPDATED_START_TIME
        defaultContractShouldBeFound("startTime.lessThan=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllContractsByStartTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where startTime is greater than DEFAULT_START_TIME
        defaultContractShouldNotBeFound("startTime.greaterThan=" + DEFAULT_START_TIME);

        // Get all the contractList where startTime is greater than SMALLER_START_TIME
        defaultContractShouldBeFound("startTime.greaterThan=" + SMALLER_START_TIME);
    }

    @Test
    @Transactional
    void getAllContractsByFinishTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where finishTime equals to DEFAULT_FINISH_TIME
        defaultContractShouldBeFound("finishTime.equals=" + DEFAULT_FINISH_TIME);

        // Get all the contractList where finishTime equals to UPDATED_FINISH_TIME
        defaultContractShouldNotBeFound("finishTime.equals=" + UPDATED_FINISH_TIME);
    }

    @Test
    @Transactional
    void getAllContractsByFinishTimeIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where finishTime in DEFAULT_FINISH_TIME or UPDATED_FINISH_TIME
        defaultContractShouldBeFound("finishTime.in=" + DEFAULT_FINISH_TIME + "," + UPDATED_FINISH_TIME);

        // Get all the contractList where finishTime equals to UPDATED_FINISH_TIME
        defaultContractShouldNotBeFound("finishTime.in=" + UPDATED_FINISH_TIME);
    }

    @Test
    @Transactional
    void getAllContractsByFinishTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where finishTime is not null
        defaultContractShouldBeFound("finishTime.specified=true");

        // Get all the contractList where finishTime is null
        defaultContractShouldNotBeFound("finishTime.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByFinishTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where finishTime is greater than or equal to DEFAULT_FINISH_TIME
        defaultContractShouldBeFound("finishTime.greaterThanOrEqual=" + DEFAULT_FINISH_TIME);

        // Get all the contractList where finishTime is greater than or equal to UPDATED_FINISH_TIME
        defaultContractShouldNotBeFound("finishTime.greaterThanOrEqual=" + UPDATED_FINISH_TIME);
    }

    @Test
    @Transactional
    void getAllContractsByFinishTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where finishTime is less than or equal to DEFAULT_FINISH_TIME
        defaultContractShouldBeFound("finishTime.lessThanOrEqual=" + DEFAULT_FINISH_TIME);

        // Get all the contractList where finishTime is less than or equal to SMALLER_FINISH_TIME
        defaultContractShouldNotBeFound("finishTime.lessThanOrEqual=" + SMALLER_FINISH_TIME);
    }

    @Test
    @Transactional
    void getAllContractsByFinishTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where finishTime is less than DEFAULT_FINISH_TIME
        defaultContractShouldNotBeFound("finishTime.lessThan=" + DEFAULT_FINISH_TIME);

        // Get all the contractList where finishTime is less than UPDATED_FINISH_TIME
        defaultContractShouldBeFound("finishTime.lessThan=" + UPDATED_FINISH_TIME);
    }

    @Test
    @Transactional
    void getAllContractsByFinishTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where finishTime is greater than DEFAULT_FINISH_TIME
        defaultContractShouldNotBeFound("finishTime.greaterThan=" + DEFAULT_FINISH_TIME);

        // Get all the contractList where finishTime is greater than SMALLER_FINISH_TIME
        defaultContractShouldBeFound("finishTime.greaterThan=" + SMALLER_FINISH_TIME);
    }

    @Test
    @Transactional
    void getAllContractsByOperatorIsEqualToSomething() throws Exception {
        Operator operator;
        if (TestUtil.findAll(em, Operator.class).isEmpty()) {
            contractRepository.saveAndFlush(contract);
            operator = OperatorResourceIT.createEntity(em);
        } else {
            operator = TestUtil.findAll(em, Operator.class).get(0);
        }
        em.persist(operator);
        em.flush();
        contract.setOperator(operator);
        contractRepository.saveAndFlush(contract);
        Long operatorId = operator.getId();

        // Get all the contractList where operator equals to operatorId
        defaultContractShouldBeFound("operatorId.equals=" + operatorId);

        // Get all the contractList where operator equals to (operatorId + 1)
        defaultContractShouldNotBeFound("operatorId.equals=" + (operatorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContractShouldBeFound(String filter) throws Exception {
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].numberInterfaceBoard").value(hasItem(DEFAULT_NUMBER_INTERFACE_BOARD.intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].finishTime").value(hasItem(sameInstant(DEFAULT_FINISH_TIME))));

        // Check, that the count call also returns 1
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContractShouldNotBeFound(String filter) throws Exception {
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContract() throws Exception {
        // Get the contract
        restContractMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract
        Contract updatedContract = contractRepository.findById(contract.getId()).get();
        // Disconnect from session so that the updates on updatedContract are not directly saved in db
        em.detach(updatedContract);
        updatedContract
            .reference(UPDATED_REFERENCE)
            .type(UPDATED_TYPE)
            .numberInterfaceBoard(UPDATED_NUMBER_INTERFACE_BOARD)
            .startTime(UPDATED_START_TIME)
            .finishTime(UPDATED_FINISH_TIME);
        ContractDTO contractDTO = contractMapper.toDto(updatedContract);

        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractDTO))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testContract.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testContract.getNumberInterfaceBoard()).isEqualTo(UPDATED_NUMBER_INTERFACE_BOARD);
        assertThat(testContract.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testContract.getFinishTime()).isEqualTo(UPDATED_FINISH_TIME);
    }

    @Test
    @Transactional
    void putNonExistingContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContractWithPatch() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract.type(UPDATED_TYPE).numberInterfaceBoard(UPDATED_NUMBER_INTERFACE_BOARD);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testContract.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testContract.getNumberInterfaceBoard()).isEqualTo(UPDATED_NUMBER_INTERFACE_BOARD);
        assertThat(testContract.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testContract.getFinishTime()).isEqualTo(DEFAULT_FINISH_TIME);
    }

    @Test
    @Transactional
    void fullUpdateContractWithPatch() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract
            .reference(UPDATED_REFERENCE)
            .type(UPDATED_TYPE)
            .numberInterfaceBoard(UPDATED_NUMBER_INTERFACE_BOARD)
            .startTime(UPDATED_START_TIME)
            .finishTime(UPDATED_FINISH_TIME);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testContract.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testContract.getNumberInterfaceBoard()).isEqualTo(UPDATED_NUMBER_INTERFACE_BOARD);
        assertThat(testContract.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testContract.getFinishTime()).isEqualTo(UPDATED_FINISH_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contractDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contractDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeDelete = contractRepository.findAll().size();

        // Delete the contract
        restContractMockMvc
            .perform(delete(ENTITY_API_URL_ID, contract.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
