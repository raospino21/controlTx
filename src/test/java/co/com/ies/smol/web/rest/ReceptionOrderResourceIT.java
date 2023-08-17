package co.com.ies.smol.web.rest;

import static co.com.ies.smol.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.PurchaseOrder;
import co.com.ies.smol.domain.ReceptionOrder;
import co.com.ies.smol.repository.ReceptionOrderRepository;
import co.com.ies.smol.service.ReceptionOrderService;
import co.com.ies.smol.service.criteria.ReceptionOrderCriteria;
import co.com.ies.smol.service.dto.ReceptionOrderDTO;
import co.com.ies.smol.service.mapper.ReceptionOrderMapper;
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
 * Integration tests for the {@link ReceptionOrderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReceptionOrderResourceIT {

    private static final Long DEFAULT_PROVIDER_LOT_NUMBER = 1L;
    private static final Long UPDATED_PROVIDER_LOT_NUMBER = 2L;
    private static final Long SMALLER_PROVIDER_LOT_NUMBER = 1L - 1L;

    private static final Long DEFAULT_AMOUNT_RECEIVED = 1L;
    private static final Long UPDATED_AMOUNT_RECEIVED = 2L;
    private static final Long SMALLER_AMOUNT_RECEIVED = 1L - 1L;

    private static final String DEFAULT_REMISSION = "AAAAAAAAAA";
    private static final String UPDATED_REMISSION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_ENTRY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ENTRY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_ENTRY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_WARRANTY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_WARRANTY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_WARRANTY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/reception-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReceptionOrderRepository receptionOrderRepository;

    @Mock
    private ReceptionOrderRepository receptionOrderRepositoryMock;

    @Autowired
    private ReceptionOrderMapper receptionOrderMapper;

    @Mock
    private ReceptionOrderService receptionOrderServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReceptionOrderMockMvc;

    private ReceptionOrder receptionOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReceptionOrder createEntity(EntityManager em) {
        ReceptionOrder receptionOrder = new ReceptionOrder()
            .providerLotNumber(DEFAULT_PROVIDER_LOT_NUMBER)
            .amountReceived(DEFAULT_AMOUNT_RECEIVED)
            .remission(DEFAULT_REMISSION)
            .entryDate(DEFAULT_ENTRY_DATE)
            .warrantyDate(DEFAULT_WARRANTY_DATE);
        // Add required entity
        PurchaseOrder purchaseOrder;
        if (TestUtil.findAll(em, PurchaseOrder.class).isEmpty()) {
            purchaseOrder = PurchaseOrderResourceIT.createEntity(em);
            em.persist(purchaseOrder);
            em.flush();
        } else {
            purchaseOrder = TestUtil.findAll(em, PurchaseOrder.class).get(0);
        }
        receptionOrder.setPurchaseOrder(purchaseOrder);
        return receptionOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReceptionOrder createUpdatedEntity(EntityManager em) {
        ReceptionOrder receptionOrder = new ReceptionOrder()
            .providerLotNumber(UPDATED_PROVIDER_LOT_NUMBER)
            .amountReceived(UPDATED_AMOUNT_RECEIVED)
            .remission(UPDATED_REMISSION)
            .entryDate(UPDATED_ENTRY_DATE)
            .warrantyDate(UPDATED_WARRANTY_DATE);
        // Add required entity
        PurchaseOrder purchaseOrder;
        if (TestUtil.findAll(em, PurchaseOrder.class).isEmpty()) {
            purchaseOrder = PurchaseOrderResourceIT.createUpdatedEntity(em);
            em.persist(purchaseOrder);
            em.flush();
        } else {
            purchaseOrder = TestUtil.findAll(em, PurchaseOrder.class).get(0);
        }
        receptionOrder.setPurchaseOrder(purchaseOrder);
        return receptionOrder;
    }

    @BeforeEach
    public void initTest() {
        receptionOrder = createEntity(em);
    }

    @Test
    @Transactional
    void createReceptionOrder() throws Exception {
        int databaseSizeBeforeCreate = receptionOrderRepository.findAll().size();
        // Create the ReceptionOrder
        ReceptionOrderDTO receptionOrderDTO = receptionOrderMapper.toDto(receptionOrder);
        restReceptionOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receptionOrderDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ReceptionOrder in the database
        List<ReceptionOrder> receptionOrderList = receptionOrderRepository.findAll();
        assertThat(receptionOrderList).hasSize(databaseSizeBeforeCreate + 1);
        ReceptionOrder testReceptionOrder = receptionOrderList.get(receptionOrderList.size() - 1);
        assertThat(testReceptionOrder.getProviderLotNumber()).isEqualTo(DEFAULT_PROVIDER_LOT_NUMBER);
        assertThat(testReceptionOrder.getAmountReceived()).isEqualTo(DEFAULT_AMOUNT_RECEIVED);
        assertThat(testReceptionOrder.getRemission()).isEqualTo(DEFAULT_REMISSION);
        assertThat(testReceptionOrder.getEntryDate()).isEqualTo(DEFAULT_ENTRY_DATE);
        assertThat(testReceptionOrder.getWarrantyDate()).isEqualTo(DEFAULT_WARRANTY_DATE);
    }

    @Test
    @Transactional
    void createReceptionOrderWithExistingId() throws Exception {
        // Create the ReceptionOrder with an existing ID
        receptionOrder.setId(1L);
        ReceptionOrderDTO receptionOrderDTO = receptionOrderMapper.toDto(receptionOrder);

        int databaseSizeBeforeCreate = receptionOrderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReceptionOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receptionOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReceptionOrder in the database
        List<ReceptionOrder> receptionOrderList = receptionOrderRepository.findAll();
        assertThat(receptionOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProviderLotNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = receptionOrderRepository.findAll().size();
        // set the field null
        receptionOrder.setProviderLotNumber(null);

        // Create the ReceptionOrder, which fails.
        ReceptionOrderDTO receptionOrderDTO = receptionOrderMapper.toDto(receptionOrder);

        restReceptionOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receptionOrderDTO))
            )
            .andExpect(status().isBadRequest());

        List<ReceptionOrder> receptionOrderList = receptionOrderRepository.findAll();
        assertThat(receptionOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountReceivedIsRequired() throws Exception {
        int databaseSizeBeforeTest = receptionOrderRepository.findAll().size();
        // set the field null
        receptionOrder.setAmountReceived(null);

        // Create the ReceptionOrder, which fails.
        ReceptionOrderDTO receptionOrderDTO = receptionOrderMapper.toDto(receptionOrder);

        restReceptionOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receptionOrderDTO))
            )
            .andExpect(status().isBadRequest());

        List<ReceptionOrder> receptionOrderList = receptionOrderRepository.findAll();
        assertThat(receptionOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRemissionIsRequired() throws Exception {
        int databaseSizeBeforeTest = receptionOrderRepository.findAll().size();
        // set the field null
        receptionOrder.setRemission(null);

        // Create the ReceptionOrder, which fails.
        ReceptionOrderDTO receptionOrderDTO = receptionOrderMapper.toDto(receptionOrder);

        restReceptionOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receptionOrderDTO))
            )
            .andExpect(status().isBadRequest());

        List<ReceptionOrder> receptionOrderList = receptionOrderRepository.findAll();
        assertThat(receptionOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntryDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = receptionOrderRepository.findAll().size();
        // set the field null
        receptionOrder.setEntryDate(null);

        // Create the ReceptionOrder, which fails.
        ReceptionOrderDTO receptionOrderDTO = receptionOrderMapper.toDto(receptionOrder);

        restReceptionOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receptionOrderDTO))
            )
            .andExpect(status().isBadRequest());

        List<ReceptionOrder> receptionOrderList = receptionOrderRepository.findAll();
        assertThat(receptionOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWarrantyDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = receptionOrderRepository.findAll().size();
        // set the field null
        receptionOrder.setWarrantyDate(null);

        // Create the ReceptionOrder, which fails.
        ReceptionOrderDTO receptionOrderDTO = receptionOrderMapper.toDto(receptionOrder);

        restReceptionOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receptionOrderDTO))
            )
            .andExpect(status().isBadRequest());

        List<ReceptionOrder> receptionOrderList = receptionOrderRepository.findAll();
        assertThat(receptionOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReceptionOrders() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList
        restReceptionOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(receptionOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].providerLotNumber").value(hasItem(DEFAULT_PROVIDER_LOT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].amountReceived").value(hasItem(DEFAULT_AMOUNT_RECEIVED.intValue())))
            .andExpect(jsonPath("$.[*].remission").value(hasItem(DEFAULT_REMISSION)))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(sameInstant(DEFAULT_ENTRY_DATE))))
            .andExpect(jsonPath("$.[*].warrantyDate").value(hasItem(sameInstant(DEFAULT_WARRANTY_DATE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReceptionOrdersWithEagerRelationshipsIsEnabled() throws Exception {
        when(receptionOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReceptionOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(receptionOrderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReceptionOrdersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(receptionOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReceptionOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(receptionOrderRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReceptionOrder() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get the receptionOrder
        restReceptionOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, receptionOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(receptionOrder.getId().intValue()))
            .andExpect(jsonPath("$.providerLotNumber").value(DEFAULT_PROVIDER_LOT_NUMBER.intValue()))
            .andExpect(jsonPath("$.amountReceived").value(DEFAULT_AMOUNT_RECEIVED.intValue()))
            .andExpect(jsonPath("$.remission").value(DEFAULT_REMISSION))
            .andExpect(jsonPath("$.entryDate").value(sameInstant(DEFAULT_ENTRY_DATE)))
            .andExpect(jsonPath("$.warrantyDate").value(sameInstant(DEFAULT_WARRANTY_DATE)));
    }

    @Test
    @Transactional
    void getReceptionOrdersByIdFiltering() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        Long id = receptionOrder.getId();

        defaultReceptionOrderShouldBeFound("id.equals=" + id);
        defaultReceptionOrderShouldNotBeFound("id.notEquals=" + id);

        defaultReceptionOrderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReceptionOrderShouldNotBeFound("id.greaterThan=" + id);

        defaultReceptionOrderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReceptionOrderShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByProviderLotNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where providerLotNumber equals to DEFAULT_PROVIDER_LOT_NUMBER
        defaultReceptionOrderShouldBeFound("providerLotNumber.equals=" + DEFAULT_PROVIDER_LOT_NUMBER);

        // Get all the receptionOrderList where providerLotNumber equals to UPDATED_PROVIDER_LOT_NUMBER
        defaultReceptionOrderShouldNotBeFound("providerLotNumber.equals=" + UPDATED_PROVIDER_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByProviderLotNumberIsInShouldWork() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where providerLotNumber in DEFAULT_PROVIDER_LOT_NUMBER or UPDATED_PROVIDER_LOT_NUMBER
        defaultReceptionOrderShouldBeFound("providerLotNumber.in=" + DEFAULT_PROVIDER_LOT_NUMBER + "," + UPDATED_PROVIDER_LOT_NUMBER);

        // Get all the receptionOrderList where providerLotNumber equals to UPDATED_PROVIDER_LOT_NUMBER
        defaultReceptionOrderShouldNotBeFound("providerLotNumber.in=" + UPDATED_PROVIDER_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByProviderLotNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where providerLotNumber is not null
        defaultReceptionOrderShouldBeFound("providerLotNumber.specified=true");

        // Get all the receptionOrderList where providerLotNumber is null
        defaultReceptionOrderShouldNotBeFound("providerLotNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByProviderLotNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where providerLotNumber is greater than or equal to DEFAULT_PROVIDER_LOT_NUMBER
        defaultReceptionOrderShouldBeFound("providerLotNumber.greaterThanOrEqual=" + DEFAULT_PROVIDER_LOT_NUMBER);

        // Get all the receptionOrderList where providerLotNumber is greater than or equal to UPDATED_PROVIDER_LOT_NUMBER
        defaultReceptionOrderShouldNotBeFound("providerLotNumber.greaterThanOrEqual=" + UPDATED_PROVIDER_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByProviderLotNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where providerLotNumber is less than or equal to DEFAULT_PROVIDER_LOT_NUMBER
        defaultReceptionOrderShouldBeFound("providerLotNumber.lessThanOrEqual=" + DEFAULT_PROVIDER_LOT_NUMBER);

        // Get all the receptionOrderList where providerLotNumber is less than or equal to SMALLER_PROVIDER_LOT_NUMBER
        defaultReceptionOrderShouldNotBeFound("providerLotNumber.lessThanOrEqual=" + SMALLER_PROVIDER_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByProviderLotNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where providerLotNumber is less than DEFAULT_PROVIDER_LOT_NUMBER
        defaultReceptionOrderShouldNotBeFound("providerLotNumber.lessThan=" + DEFAULT_PROVIDER_LOT_NUMBER);

        // Get all the receptionOrderList where providerLotNumber is less than UPDATED_PROVIDER_LOT_NUMBER
        defaultReceptionOrderShouldBeFound("providerLotNumber.lessThan=" + UPDATED_PROVIDER_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByProviderLotNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where providerLotNumber is greater than DEFAULT_PROVIDER_LOT_NUMBER
        defaultReceptionOrderShouldNotBeFound("providerLotNumber.greaterThan=" + DEFAULT_PROVIDER_LOT_NUMBER);

        // Get all the receptionOrderList where providerLotNumber is greater than SMALLER_PROVIDER_LOT_NUMBER
        defaultReceptionOrderShouldBeFound("providerLotNumber.greaterThan=" + SMALLER_PROVIDER_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByAmountReceivedIsEqualToSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where amountReceived equals to DEFAULT_AMOUNT_RECEIVED
        defaultReceptionOrderShouldBeFound("amountReceived.equals=" + DEFAULT_AMOUNT_RECEIVED);

        // Get all the receptionOrderList where amountReceived equals to UPDATED_AMOUNT_RECEIVED
        defaultReceptionOrderShouldNotBeFound("amountReceived.equals=" + UPDATED_AMOUNT_RECEIVED);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByAmountReceivedIsInShouldWork() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where amountReceived in DEFAULT_AMOUNT_RECEIVED or UPDATED_AMOUNT_RECEIVED
        defaultReceptionOrderShouldBeFound("amountReceived.in=" + DEFAULT_AMOUNT_RECEIVED + "," + UPDATED_AMOUNT_RECEIVED);

        // Get all the receptionOrderList where amountReceived equals to UPDATED_AMOUNT_RECEIVED
        defaultReceptionOrderShouldNotBeFound("amountReceived.in=" + UPDATED_AMOUNT_RECEIVED);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByAmountReceivedIsNullOrNotNull() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where amountReceived is not null
        defaultReceptionOrderShouldBeFound("amountReceived.specified=true");

        // Get all the receptionOrderList where amountReceived is null
        defaultReceptionOrderShouldNotBeFound("amountReceived.specified=false");
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByAmountReceivedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where amountReceived is greater than or equal to DEFAULT_AMOUNT_RECEIVED
        defaultReceptionOrderShouldBeFound("amountReceived.greaterThanOrEqual=" + DEFAULT_AMOUNT_RECEIVED);

        // Get all the receptionOrderList where amountReceived is greater than or equal to UPDATED_AMOUNT_RECEIVED
        defaultReceptionOrderShouldNotBeFound("amountReceived.greaterThanOrEqual=" + UPDATED_AMOUNT_RECEIVED);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByAmountReceivedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where amountReceived is less than or equal to DEFAULT_AMOUNT_RECEIVED
        defaultReceptionOrderShouldBeFound("amountReceived.lessThanOrEqual=" + DEFAULT_AMOUNT_RECEIVED);

        // Get all the receptionOrderList where amountReceived is less than or equal to SMALLER_AMOUNT_RECEIVED
        defaultReceptionOrderShouldNotBeFound("amountReceived.lessThanOrEqual=" + SMALLER_AMOUNT_RECEIVED);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByAmountReceivedIsLessThanSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where amountReceived is less than DEFAULT_AMOUNT_RECEIVED
        defaultReceptionOrderShouldNotBeFound("amountReceived.lessThan=" + DEFAULT_AMOUNT_RECEIVED);

        // Get all the receptionOrderList where amountReceived is less than UPDATED_AMOUNT_RECEIVED
        defaultReceptionOrderShouldBeFound("amountReceived.lessThan=" + UPDATED_AMOUNT_RECEIVED);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByAmountReceivedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where amountReceived is greater than DEFAULT_AMOUNT_RECEIVED
        defaultReceptionOrderShouldNotBeFound("amountReceived.greaterThan=" + DEFAULT_AMOUNT_RECEIVED);

        // Get all the receptionOrderList where amountReceived is greater than SMALLER_AMOUNT_RECEIVED
        defaultReceptionOrderShouldBeFound("amountReceived.greaterThan=" + SMALLER_AMOUNT_RECEIVED);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByRemissionIsEqualToSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where remission equals to DEFAULT_REMISSION
        defaultReceptionOrderShouldBeFound("remission.equals=" + DEFAULT_REMISSION);

        // Get all the receptionOrderList where remission equals to UPDATED_REMISSION
        defaultReceptionOrderShouldNotBeFound("remission.equals=" + UPDATED_REMISSION);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByRemissionIsInShouldWork() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where remission in DEFAULT_REMISSION or UPDATED_REMISSION
        defaultReceptionOrderShouldBeFound("remission.in=" + DEFAULT_REMISSION + "," + UPDATED_REMISSION);

        // Get all the receptionOrderList where remission equals to UPDATED_REMISSION
        defaultReceptionOrderShouldNotBeFound("remission.in=" + UPDATED_REMISSION);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByRemissionIsNullOrNotNull() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where remission is not null
        defaultReceptionOrderShouldBeFound("remission.specified=true");

        // Get all the receptionOrderList where remission is null
        defaultReceptionOrderShouldNotBeFound("remission.specified=false");
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByRemissionContainsSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where remission contains DEFAULT_REMISSION
        defaultReceptionOrderShouldBeFound("remission.contains=" + DEFAULT_REMISSION);

        // Get all the receptionOrderList where remission contains UPDATED_REMISSION
        defaultReceptionOrderShouldNotBeFound("remission.contains=" + UPDATED_REMISSION);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByRemissionNotContainsSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where remission does not contain DEFAULT_REMISSION
        defaultReceptionOrderShouldNotBeFound("remission.doesNotContain=" + DEFAULT_REMISSION);

        // Get all the receptionOrderList where remission does not contain UPDATED_REMISSION
        defaultReceptionOrderShouldBeFound("remission.doesNotContain=" + UPDATED_REMISSION);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByEntryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where entryDate equals to DEFAULT_ENTRY_DATE
        defaultReceptionOrderShouldBeFound("entryDate.equals=" + DEFAULT_ENTRY_DATE);

        // Get all the receptionOrderList where entryDate equals to UPDATED_ENTRY_DATE
        defaultReceptionOrderShouldNotBeFound("entryDate.equals=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByEntryDateIsInShouldWork() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where entryDate in DEFAULT_ENTRY_DATE or UPDATED_ENTRY_DATE
        defaultReceptionOrderShouldBeFound("entryDate.in=" + DEFAULT_ENTRY_DATE + "," + UPDATED_ENTRY_DATE);

        // Get all the receptionOrderList where entryDate equals to UPDATED_ENTRY_DATE
        defaultReceptionOrderShouldNotBeFound("entryDate.in=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByEntryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where entryDate is not null
        defaultReceptionOrderShouldBeFound("entryDate.specified=true");

        // Get all the receptionOrderList where entryDate is null
        defaultReceptionOrderShouldNotBeFound("entryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByEntryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where entryDate is greater than or equal to DEFAULT_ENTRY_DATE
        defaultReceptionOrderShouldBeFound("entryDate.greaterThanOrEqual=" + DEFAULT_ENTRY_DATE);

        // Get all the receptionOrderList where entryDate is greater than or equal to UPDATED_ENTRY_DATE
        defaultReceptionOrderShouldNotBeFound("entryDate.greaterThanOrEqual=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByEntryDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where entryDate is less than or equal to DEFAULT_ENTRY_DATE
        defaultReceptionOrderShouldBeFound("entryDate.lessThanOrEqual=" + DEFAULT_ENTRY_DATE);

        // Get all the receptionOrderList where entryDate is less than or equal to SMALLER_ENTRY_DATE
        defaultReceptionOrderShouldNotBeFound("entryDate.lessThanOrEqual=" + SMALLER_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByEntryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where entryDate is less than DEFAULT_ENTRY_DATE
        defaultReceptionOrderShouldNotBeFound("entryDate.lessThan=" + DEFAULT_ENTRY_DATE);

        // Get all the receptionOrderList where entryDate is less than UPDATED_ENTRY_DATE
        defaultReceptionOrderShouldBeFound("entryDate.lessThan=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByEntryDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where entryDate is greater than DEFAULT_ENTRY_DATE
        defaultReceptionOrderShouldNotBeFound("entryDate.greaterThan=" + DEFAULT_ENTRY_DATE);

        // Get all the receptionOrderList where entryDate is greater than SMALLER_ENTRY_DATE
        defaultReceptionOrderShouldBeFound("entryDate.greaterThan=" + SMALLER_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByWarrantyDateIsEqualToSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where warrantyDate equals to DEFAULT_WARRANTY_DATE
        defaultReceptionOrderShouldBeFound("warrantyDate.equals=" + DEFAULT_WARRANTY_DATE);

        // Get all the receptionOrderList where warrantyDate equals to UPDATED_WARRANTY_DATE
        defaultReceptionOrderShouldNotBeFound("warrantyDate.equals=" + UPDATED_WARRANTY_DATE);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByWarrantyDateIsInShouldWork() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where warrantyDate in DEFAULT_WARRANTY_DATE or UPDATED_WARRANTY_DATE
        defaultReceptionOrderShouldBeFound("warrantyDate.in=" + DEFAULT_WARRANTY_DATE + "," + UPDATED_WARRANTY_DATE);

        // Get all the receptionOrderList where warrantyDate equals to UPDATED_WARRANTY_DATE
        defaultReceptionOrderShouldNotBeFound("warrantyDate.in=" + UPDATED_WARRANTY_DATE);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByWarrantyDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where warrantyDate is not null
        defaultReceptionOrderShouldBeFound("warrantyDate.specified=true");

        // Get all the receptionOrderList where warrantyDate is null
        defaultReceptionOrderShouldNotBeFound("warrantyDate.specified=false");
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByWarrantyDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where warrantyDate is greater than or equal to DEFAULT_WARRANTY_DATE
        defaultReceptionOrderShouldBeFound("warrantyDate.greaterThanOrEqual=" + DEFAULT_WARRANTY_DATE);

        // Get all the receptionOrderList where warrantyDate is greater than or equal to UPDATED_WARRANTY_DATE
        defaultReceptionOrderShouldNotBeFound("warrantyDate.greaterThanOrEqual=" + UPDATED_WARRANTY_DATE);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByWarrantyDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where warrantyDate is less than or equal to DEFAULT_WARRANTY_DATE
        defaultReceptionOrderShouldBeFound("warrantyDate.lessThanOrEqual=" + DEFAULT_WARRANTY_DATE);

        // Get all the receptionOrderList where warrantyDate is less than or equal to SMALLER_WARRANTY_DATE
        defaultReceptionOrderShouldNotBeFound("warrantyDate.lessThanOrEqual=" + SMALLER_WARRANTY_DATE);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByWarrantyDateIsLessThanSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where warrantyDate is less than DEFAULT_WARRANTY_DATE
        defaultReceptionOrderShouldNotBeFound("warrantyDate.lessThan=" + DEFAULT_WARRANTY_DATE);

        // Get all the receptionOrderList where warrantyDate is less than UPDATED_WARRANTY_DATE
        defaultReceptionOrderShouldBeFound("warrantyDate.lessThan=" + UPDATED_WARRANTY_DATE);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByWarrantyDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        // Get all the receptionOrderList where warrantyDate is greater than DEFAULT_WARRANTY_DATE
        defaultReceptionOrderShouldNotBeFound("warrantyDate.greaterThan=" + DEFAULT_WARRANTY_DATE);

        // Get all the receptionOrderList where warrantyDate is greater than SMALLER_WARRANTY_DATE
        defaultReceptionOrderShouldBeFound("warrantyDate.greaterThan=" + SMALLER_WARRANTY_DATE);
    }

    @Test
    @Transactional
    void getAllReceptionOrdersByPurchaseOrderIsEqualToSomething() throws Exception {
        PurchaseOrder purchaseOrder;
        if (TestUtil.findAll(em, PurchaseOrder.class).isEmpty()) {
            receptionOrderRepository.saveAndFlush(receptionOrder);
            purchaseOrder = PurchaseOrderResourceIT.createEntity(em);
        } else {
            purchaseOrder = TestUtil.findAll(em, PurchaseOrder.class).get(0);
        }
        em.persist(purchaseOrder);
        em.flush();
        receptionOrder.setPurchaseOrder(purchaseOrder);
        receptionOrderRepository.saveAndFlush(receptionOrder);
        Long purchaseOrderId = purchaseOrder.getId();

        // Get all the receptionOrderList where purchaseOrder equals to purchaseOrderId
        defaultReceptionOrderShouldBeFound("purchaseOrderId.equals=" + purchaseOrderId);

        // Get all the receptionOrderList where purchaseOrder equals to (purchaseOrderId + 1)
        defaultReceptionOrderShouldNotBeFound("purchaseOrderId.equals=" + (purchaseOrderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReceptionOrderShouldBeFound(String filter) throws Exception {
        restReceptionOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(receptionOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].providerLotNumber").value(hasItem(DEFAULT_PROVIDER_LOT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].amountReceived").value(hasItem(DEFAULT_AMOUNT_RECEIVED.intValue())))
            .andExpect(jsonPath("$.[*].remission").value(hasItem(DEFAULT_REMISSION)))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(sameInstant(DEFAULT_ENTRY_DATE))))
            .andExpect(jsonPath("$.[*].warrantyDate").value(hasItem(sameInstant(DEFAULT_WARRANTY_DATE))));

        // Check, that the count call also returns 1
        restReceptionOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReceptionOrderShouldNotBeFound(String filter) throws Exception {
        restReceptionOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReceptionOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReceptionOrder() throws Exception {
        // Get the receptionOrder
        restReceptionOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReceptionOrder() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        int databaseSizeBeforeUpdate = receptionOrderRepository.findAll().size();

        // Update the receptionOrder
        ReceptionOrder updatedReceptionOrder = receptionOrderRepository.findById(receptionOrder.getId()).get();
        // Disconnect from session so that the updates on updatedReceptionOrder are not directly saved in db
        em.detach(updatedReceptionOrder);
        updatedReceptionOrder
            .providerLotNumber(UPDATED_PROVIDER_LOT_NUMBER)
            .amountReceived(UPDATED_AMOUNT_RECEIVED)
            .remission(UPDATED_REMISSION)
            .entryDate(UPDATED_ENTRY_DATE)
            .warrantyDate(UPDATED_WARRANTY_DATE);
        ReceptionOrderDTO receptionOrderDTO = receptionOrderMapper.toDto(updatedReceptionOrder);

        restReceptionOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, receptionOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(receptionOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReceptionOrder in the database
        List<ReceptionOrder> receptionOrderList = receptionOrderRepository.findAll();
        assertThat(receptionOrderList).hasSize(databaseSizeBeforeUpdate);
        ReceptionOrder testReceptionOrder = receptionOrderList.get(receptionOrderList.size() - 1);
        assertThat(testReceptionOrder.getProviderLotNumber()).isEqualTo(UPDATED_PROVIDER_LOT_NUMBER);
        assertThat(testReceptionOrder.getAmountReceived()).isEqualTo(UPDATED_AMOUNT_RECEIVED);
        assertThat(testReceptionOrder.getRemission()).isEqualTo(UPDATED_REMISSION);
        assertThat(testReceptionOrder.getEntryDate()).isEqualTo(UPDATED_ENTRY_DATE);
        assertThat(testReceptionOrder.getWarrantyDate()).isEqualTo(UPDATED_WARRANTY_DATE);
    }

    @Test
    @Transactional
    void putNonExistingReceptionOrder() throws Exception {
        int databaseSizeBeforeUpdate = receptionOrderRepository.findAll().size();
        receptionOrder.setId(count.incrementAndGet());

        // Create the ReceptionOrder
        ReceptionOrderDTO receptionOrderDTO = receptionOrderMapper.toDto(receptionOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReceptionOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, receptionOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(receptionOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReceptionOrder in the database
        List<ReceptionOrder> receptionOrderList = receptionOrderRepository.findAll();
        assertThat(receptionOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReceptionOrder() throws Exception {
        int databaseSizeBeforeUpdate = receptionOrderRepository.findAll().size();
        receptionOrder.setId(count.incrementAndGet());

        // Create the ReceptionOrder
        ReceptionOrderDTO receptionOrderDTO = receptionOrderMapper.toDto(receptionOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceptionOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(receptionOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReceptionOrder in the database
        List<ReceptionOrder> receptionOrderList = receptionOrderRepository.findAll();
        assertThat(receptionOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReceptionOrder() throws Exception {
        int databaseSizeBeforeUpdate = receptionOrderRepository.findAll().size();
        receptionOrder.setId(count.incrementAndGet());

        // Create the ReceptionOrder
        ReceptionOrderDTO receptionOrderDTO = receptionOrderMapper.toDto(receptionOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceptionOrderMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receptionOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReceptionOrder in the database
        List<ReceptionOrder> receptionOrderList = receptionOrderRepository.findAll();
        assertThat(receptionOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReceptionOrderWithPatch() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        int databaseSizeBeforeUpdate = receptionOrderRepository.findAll().size();

        // Update the receptionOrder using partial update
        ReceptionOrder partialUpdatedReceptionOrder = new ReceptionOrder();
        partialUpdatedReceptionOrder.setId(receptionOrder.getId());

        partialUpdatedReceptionOrder.providerLotNumber(UPDATED_PROVIDER_LOT_NUMBER).warrantyDate(UPDATED_WARRANTY_DATE);

        restReceptionOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReceptionOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReceptionOrder))
            )
            .andExpect(status().isOk());

        // Validate the ReceptionOrder in the database
        List<ReceptionOrder> receptionOrderList = receptionOrderRepository.findAll();
        assertThat(receptionOrderList).hasSize(databaseSizeBeforeUpdate);
        ReceptionOrder testReceptionOrder = receptionOrderList.get(receptionOrderList.size() - 1);
        assertThat(testReceptionOrder.getProviderLotNumber()).isEqualTo(UPDATED_PROVIDER_LOT_NUMBER);
        assertThat(testReceptionOrder.getAmountReceived()).isEqualTo(DEFAULT_AMOUNT_RECEIVED);
        assertThat(testReceptionOrder.getRemission()).isEqualTo(DEFAULT_REMISSION);
        assertThat(testReceptionOrder.getEntryDate()).isEqualTo(DEFAULT_ENTRY_DATE);
        assertThat(testReceptionOrder.getWarrantyDate()).isEqualTo(UPDATED_WARRANTY_DATE);
    }

    @Test
    @Transactional
    void fullUpdateReceptionOrderWithPatch() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        int databaseSizeBeforeUpdate = receptionOrderRepository.findAll().size();

        // Update the receptionOrder using partial update
        ReceptionOrder partialUpdatedReceptionOrder = new ReceptionOrder();
        partialUpdatedReceptionOrder.setId(receptionOrder.getId());

        partialUpdatedReceptionOrder
            .providerLotNumber(UPDATED_PROVIDER_LOT_NUMBER)
            .amountReceived(UPDATED_AMOUNT_RECEIVED)
            .remission(UPDATED_REMISSION)
            .entryDate(UPDATED_ENTRY_DATE)
            .warrantyDate(UPDATED_WARRANTY_DATE);

        restReceptionOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReceptionOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReceptionOrder))
            )
            .andExpect(status().isOk());

        // Validate the ReceptionOrder in the database
        List<ReceptionOrder> receptionOrderList = receptionOrderRepository.findAll();
        assertThat(receptionOrderList).hasSize(databaseSizeBeforeUpdate);
        ReceptionOrder testReceptionOrder = receptionOrderList.get(receptionOrderList.size() - 1);
        assertThat(testReceptionOrder.getProviderLotNumber()).isEqualTo(UPDATED_PROVIDER_LOT_NUMBER);
        assertThat(testReceptionOrder.getAmountReceived()).isEqualTo(UPDATED_AMOUNT_RECEIVED);
        assertThat(testReceptionOrder.getRemission()).isEqualTo(UPDATED_REMISSION);
        assertThat(testReceptionOrder.getEntryDate()).isEqualTo(UPDATED_ENTRY_DATE);
        assertThat(testReceptionOrder.getWarrantyDate()).isEqualTo(UPDATED_WARRANTY_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingReceptionOrder() throws Exception {
        int databaseSizeBeforeUpdate = receptionOrderRepository.findAll().size();
        receptionOrder.setId(count.incrementAndGet());

        // Create the ReceptionOrder
        ReceptionOrderDTO receptionOrderDTO = receptionOrderMapper.toDto(receptionOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReceptionOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, receptionOrderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(receptionOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReceptionOrder in the database
        List<ReceptionOrder> receptionOrderList = receptionOrderRepository.findAll();
        assertThat(receptionOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReceptionOrder() throws Exception {
        int databaseSizeBeforeUpdate = receptionOrderRepository.findAll().size();
        receptionOrder.setId(count.incrementAndGet());

        // Create the ReceptionOrder
        ReceptionOrderDTO receptionOrderDTO = receptionOrderMapper.toDto(receptionOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceptionOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(receptionOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReceptionOrder in the database
        List<ReceptionOrder> receptionOrderList = receptionOrderRepository.findAll();
        assertThat(receptionOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReceptionOrder() throws Exception {
        int databaseSizeBeforeUpdate = receptionOrderRepository.findAll().size();
        receptionOrder.setId(count.incrementAndGet());

        // Create the ReceptionOrder
        ReceptionOrderDTO receptionOrderDTO = receptionOrderMapper.toDto(receptionOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceptionOrderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(receptionOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReceptionOrder in the database
        List<ReceptionOrder> receptionOrderList = receptionOrderRepository.findAll();
        assertThat(receptionOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReceptionOrder() throws Exception {
        // Initialize the database
        receptionOrderRepository.saveAndFlush(receptionOrder);

        int databaseSizeBeforeDelete = receptionOrderRepository.findAll().size();

        // Delete the receptionOrder
        restReceptionOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, receptionOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReceptionOrder> receptionOrderList = receptionOrderRepository.findAll();
        assertThat(receptionOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
