package co.com.ies.smol.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.InterfaceBoard;
import co.com.ies.smol.domain.ReceptionOrder;
import co.com.ies.smol.repository.InterfaceBoardRepository;
import co.com.ies.smol.service.InterfaceBoardService;
import co.com.ies.smol.service.criteria.InterfaceBoardCriteria;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import co.com.ies.smol.service.mapper.InterfaceBoardMapper;
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
 * Integration tests for the {@link InterfaceBoardResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InterfaceBoardResourceIT {

    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_HASH = "AAAAAAAAAA";
    private static final String UPDATED_HASH = "BBBBBBBBBB";

    private static final String DEFAULT_MAC = "AAAAAAAAAA";
    private static final String UPDATED_MAC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/interface-boards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InterfaceBoardRepository interfaceBoardRepository;

    @Mock
    private InterfaceBoardRepository interfaceBoardRepositoryMock;

    @Autowired
    private InterfaceBoardMapper interfaceBoardMapper;

    @Mock
    private InterfaceBoardService interfaceBoardServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInterfaceBoardMockMvc;

    private InterfaceBoard interfaceBoard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InterfaceBoard createEntity(EntityManager em) {
        InterfaceBoard interfaceBoard = new InterfaceBoard().ipAddress(DEFAULT_IP_ADDRESS).hash(DEFAULT_HASH).mac(DEFAULT_MAC);
        // Add required entity
        ReceptionOrder receptionOrder;
        if (TestUtil.findAll(em, ReceptionOrder.class).isEmpty()) {
            receptionOrder = ReceptionOrderResourceIT.createEntity(em);
            em.persist(receptionOrder);
            em.flush();
        } else {
            receptionOrder = TestUtil.findAll(em, ReceptionOrder.class).get(0);
        }
        interfaceBoard.setReceptionOrder(receptionOrder);
        return interfaceBoard;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InterfaceBoard createUpdatedEntity(EntityManager em) {
        InterfaceBoard interfaceBoard = new InterfaceBoard().ipAddress(UPDATED_IP_ADDRESS).hash(UPDATED_HASH).mac(UPDATED_MAC);
        // Add required entity
        ReceptionOrder receptionOrder;
        if (TestUtil.findAll(em, ReceptionOrder.class).isEmpty()) {
            receptionOrder = ReceptionOrderResourceIT.createUpdatedEntity(em);
            em.persist(receptionOrder);
            em.flush();
        } else {
            receptionOrder = TestUtil.findAll(em, ReceptionOrder.class).get(0);
        }
        interfaceBoard.setReceptionOrder(receptionOrder);
        return interfaceBoard;
    }

    @BeforeEach
    public void initTest() {
        interfaceBoard = createEntity(em);
    }

    @Test
    @Transactional
    void createInterfaceBoard() throws Exception {
        int databaseSizeBeforeCreate = interfaceBoardRepository.findAll().size();
        // Create the InterfaceBoard
        InterfaceBoardDTO interfaceBoardDTO = interfaceBoardMapper.toDto(interfaceBoard);
        restInterfaceBoardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interfaceBoardDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeCreate + 1);
        InterfaceBoard testInterfaceBoard = interfaceBoardList.get(interfaceBoardList.size() - 1);
        assertThat(testInterfaceBoard.getIpAddress()).isEqualTo(DEFAULT_IP_ADDRESS);
        assertThat(testInterfaceBoard.getHash()).isEqualTo(DEFAULT_HASH);
        assertThat(testInterfaceBoard.getMac()).isEqualTo(DEFAULT_MAC);
    }

    @Test
    @Transactional
    void createInterfaceBoardWithExistingId() throws Exception {
        // Create the InterfaceBoard with an existing ID
        interfaceBoard.setId(1L);
        InterfaceBoardDTO interfaceBoardDTO = interfaceBoardMapper.toDto(interfaceBoard);

        int databaseSizeBeforeCreate = interfaceBoardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterfaceBoardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interfaceBoardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMacIsRequired() throws Exception {
        int databaseSizeBeforeTest = interfaceBoardRepository.findAll().size();
        // set the field null
        interfaceBoard.setMac(null);

        // Create the InterfaceBoard, which fails.
        InterfaceBoardDTO interfaceBoardDTO = interfaceBoardMapper.toDto(interfaceBoard);

        restInterfaceBoardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interfaceBoardDTO))
            )
            .andExpect(status().isBadRequest());

        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInterfaceBoards() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList
        restInterfaceBoardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interfaceBoard.getId().intValue())))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH)))
            .andExpect(jsonPath("$.[*].mac").value(hasItem(DEFAULT_MAC)));
    }

    @Test
    @Transactional
    void getInterfaceBoard() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get the interfaceBoard
        restInterfaceBoardMockMvc
            .perform(get(ENTITY_API_URL_ID, interfaceBoard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(interfaceBoard.getId().intValue()))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS))
            .andExpect(jsonPath("$.hash").value(DEFAULT_HASH))
            .andExpect(jsonPath("$.mac").value(DEFAULT_MAC));
    }

    @Test
    @Transactional
    void getInterfaceBoardsByIdFiltering() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        Long id = interfaceBoard.getId();

        defaultInterfaceBoardShouldBeFound("id.equals=" + id);
        defaultInterfaceBoardShouldNotBeFound("id.notEquals=" + id);

        defaultInterfaceBoardShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInterfaceBoardShouldNotBeFound("id.greaterThan=" + id);

        defaultInterfaceBoardShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInterfaceBoardShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByIpAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where ipAddress equals to DEFAULT_IP_ADDRESS
        defaultInterfaceBoardShouldBeFound("ipAddress.equals=" + DEFAULT_IP_ADDRESS);

        // Get all the interfaceBoardList where ipAddress equals to UPDATED_IP_ADDRESS
        defaultInterfaceBoardShouldNotBeFound("ipAddress.equals=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByIpAddressIsInShouldWork() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where ipAddress in DEFAULT_IP_ADDRESS or UPDATED_IP_ADDRESS
        defaultInterfaceBoardShouldBeFound("ipAddress.in=" + DEFAULT_IP_ADDRESS + "," + UPDATED_IP_ADDRESS);

        // Get all the interfaceBoardList where ipAddress equals to UPDATED_IP_ADDRESS
        defaultInterfaceBoardShouldNotBeFound("ipAddress.in=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByIpAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where ipAddress is not null
        defaultInterfaceBoardShouldBeFound("ipAddress.specified=true");

        // Get all the interfaceBoardList where ipAddress is null
        defaultInterfaceBoardShouldNotBeFound("ipAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByIpAddressContainsSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where ipAddress contains DEFAULT_IP_ADDRESS
        defaultInterfaceBoardShouldBeFound("ipAddress.contains=" + DEFAULT_IP_ADDRESS);

        // Get all the interfaceBoardList where ipAddress contains UPDATED_IP_ADDRESS
        defaultInterfaceBoardShouldNotBeFound("ipAddress.contains=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByIpAddressNotContainsSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where ipAddress does not contain DEFAULT_IP_ADDRESS
        defaultInterfaceBoardShouldNotBeFound("ipAddress.doesNotContain=" + DEFAULT_IP_ADDRESS);

        // Get all the interfaceBoardList where ipAddress does not contain UPDATED_IP_ADDRESS
        defaultInterfaceBoardShouldBeFound("ipAddress.doesNotContain=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByHashIsEqualToSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where hash equals to DEFAULT_HASH
        defaultInterfaceBoardShouldBeFound("hash.equals=" + DEFAULT_HASH);

        // Get all the interfaceBoardList where hash equals to UPDATED_HASH
        defaultInterfaceBoardShouldNotBeFound("hash.equals=" + UPDATED_HASH);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByHashIsInShouldWork() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where hash in DEFAULT_HASH or UPDATED_HASH
        defaultInterfaceBoardShouldBeFound("hash.in=" + DEFAULT_HASH + "," + UPDATED_HASH);

        // Get all the interfaceBoardList where hash equals to UPDATED_HASH
        defaultInterfaceBoardShouldNotBeFound("hash.in=" + UPDATED_HASH);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByHashIsNullOrNotNull() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where hash is not null
        defaultInterfaceBoardShouldBeFound("hash.specified=true");

        // Get all the interfaceBoardList where hash is null
        defaultInterfaceBoardShouldNotBeFound("hash.specified=false");
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByHashContainsSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where hash contains DEFAULT_HASH
        defaultInterfaceBoardShouldBeFound("hash.contains=" + DEFAULT_HASH);

        // Get all the interfaceBoardList where hash contains UPDATED_HASH
        defaultInterfaceBoardShouldNotBeFound("hash.contains=" + UPDATED_HASH);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByHashNotContainsSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where hash does not contain DEFAULT_HASH
        defaultInterfaceBoardShouldNotBeFound("hash.doesNotContain=" + DEFAULT_HASH);

        // Get all the interfaceBoardList where hash does not contain UPDATED_HASH
        defaultInterfaceBoardShouldBeFound("hash.doesNotContain=" + UPDATED_HASH);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByMacIsEqualToSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where mac equals to DEFAULT_MAC
        defaultInterfaceBoardShouldBeFound("mac.equals=" + DEFAULT_MAC);

        // Get all the interfaceBoardList where mac equals to UPDATED_MAC
        defaultInterfaceBoardShouldNotBeFound("mac.equals=" + UPDATED_MAC);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByMacIsInShouldWork() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where mac in DEFAULT_MAC or UPDATED_MAC
        defaultInterfaceBoardShouldBeFound("mac.in=" + DEFAULT_MAC + "," + UPDATED_MAC);

        // Get all the interfaceBoardList where mac equals to UPDATED_MAC
        defaultInterfaceBoardShouldNotBeFound("mac.in=" + UPDATED_MAC);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByMacIsNullOrNotNull() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where mac is not null
        defaultInterfaceBoardShouldBeFound("mac.specified=true");

        // Get all the interfaceBoardList where mac is null
        defaultInterfaceBoardShouldNotBeFound("mac.specified=false");
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByMacContainsSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where mac contains DEFAULT_MAC
        defaultInterfaceBoardShouldBeFound("mac.contains=" + DEFAULT_MAC);

        // Get all the interfaceBoardList where mac contains UPDATED_MAC
        defaultInterfaceBoardShouldNotBeFound("mac.contains=" + UPDATED_MAC);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByMacNotContainsSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where mac does not contain DEFAULT_MAC
        defaultInterfaceBoardShouldNotBeFound("mac.doesNotContain=" + DEFAULT_MAC);

        // Get all the interfaceBoardList where mac does not contain UPDATED_MAC
        defaultInterfaceBoardShouldBeFound("mac.doesNotContain=" + UPDATED_MAC);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByReceptionOrderIsEqualToSomething() throws Exception {
        ReceptionOrder receptionOrder;
        if (TestUtil.findAll(em, ReceptionOrder.class).isEmpty()) {
            interfaceBoardRepository.saveAndFlush(interfaceBoard);
            receptionOrder = ReceptionOrderResourceIT.createEntity(em);
        } else {
            receptionOrder = TestUtil.findAll(em, ReceptionOrder.class).get(0);
        }
        em.persist(receptionOrder);
        em.flush();
        interfaceBoard.setReceptionOrder(receptionOrder);
        interfaceBoardRepository.saveAndFlush(interfaceBoard);
        Long receptionOrderId = receptionOrder.getId();

        // Get all the interfaceBoardList where receptionOrder equals to receptionOrderId
        defaultInterfaceBoardShouldBeFound("receptionOrderId.equals=" + receptionOrderId);

        // Get all the interfaceBoardList where receptionOrder equals to (receptionOrderId + 1)
        defaultInterfaceBoardShouldNotBeFound("receptionOrderId.equals=" + (receptionOrderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInterfaceBoardShouldBeFound(String filter) throws Exception {
        restInterfaceBoardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interfaceBoard.getId().intValue())))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH)))
            .andExpect(jsonPath("$.[*].mac").value(hasItem(DEFAULT_MAC)));

        // Check, that the count call also returns 1
        restInterfaceBoardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInterfaceBoardShouldNotBeFound(String filter) throws Exception {
        restInterfaceBoardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInterfaceBoardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInterfaceBoard() throws Exception {
        // Get the interfaceBoard
        restInterfaceBoardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInterfaceBoard() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        int databaseSizeBeforeUpdate = interfaceBoardRepository.findAll().size();

        // Update the interfaceBoard
        InterfaceBoard updatedInterfaceBoard = interfaceBoardRepository.findById(interfaceBoard.getId()).get();
        // Disconnect from session so that the updates on updatedInterfaceBoard are not directly saved in db
        em.detach(updatedInterfaceBoard);
        updatedInterfaceBoard.ipAddress(UPDATED_IP_ADDRESS).hash(UPDATED_HASH).mac(UPDATED_MAC);
        InterfaceBoardDTO interfaceBoardDTO = interfaceBoardMapper.toDto(updatedInterfaceBoard);

        restInterfaceBoardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, interfaceBoardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interfaceBoardDTO))
            )
            .andExpect(status().isOk());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeUpdate);
        InterfaceBoard testInterfaceBoard = interfaceBoardList.get(interfaceBoardList.size() - 1);
        assertThat(testInterfaceBoard.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testInterfaceBoard.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testInterfaceBoard.getMac()).isEqualTo(UPDATED_MAC);
    }

    @Test
    @Transactional
    void putNonExistingInterfaceBoard() throws Exception {
        int databaseSizeBeforeUpdate = interfaceBoardRepository.findAll().size();
        interfaceBoard.setId(count.incrementAndGet());

        // Create the InterfaceBoard
        InterfaceBoardDTO interfaceBoardDTO = interfaceBoardMapper.toDto(interfaceBoard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterfaceBoardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, interfaceBoardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interfaceBoardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInterfaceBoard() throws Exception {
        int databaseSizeBeforeUpdate = interfaceBoardRepository.findAll().size();
        interfaceBoard.setId(count.incrementAndGet());

        // Create the InterfaceBoard
        InterfaceBoardDTO interfaceBoardDTO = interfaceBoardMapper.toDto(interfaceBoard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterfaceBoardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interfaceBoardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInterfaceBoard() throws Exception {
        int databaseSizeBeforeUpdate = interfaceBoardRepository.findAll().size();
        interfaceBoard.setId(count.incrementAndGet());

        // Create the InterfaceBoard
        InterfaceBoardDTO interfaceBoardDTO = interfaceBoardMapper.toDto(interfaceBoard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterfaceBoardMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interfaceBoardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInterfaceBoardWithPatch() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        int databaseSizeBeforeUpdate = interfaceBoardRepository.findAll().size();

        // Update the interfaceBoard using partial update
        InterfaceBoard partialUpdatedInterfaceBoard = new InterfaceBoard();
        partialUpdatedInterfaceBoard.setId(interfaceBoard.getId());

        partialUpdatedInterfaceBoard.ipAddress(UPDATED_IP_ADDRESS).hash(UPDATED_HASH);

        restInterfaceBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInterfaceBoard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInterfaceBoard))
            )
            .andExpect(status().isOk());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeUpdate);
        InterfaceBoard testInterfaceBoard = interfaceBoardList.get(interfaceBoardList.size() - 1);
        assertThat(testInterfaceBoard.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testInterfaceBoard.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testInterfaceBoard.getMac()).isEqualTo(DEFAULT_MAC);
    }

    @Test
    @Transactional
    void fullUpdateInterfaceBoardWithPatch() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        int databaseSizeBeforeUpdate = interfaceBoardRepository.findAll().size();

        // Update the interfaceBoard using partial update
        InterfaceBoard partialUpdatedInterfaceBoard = new InterfaceBoard();
        partialUpdatedInterfaceBoard.setId(interfaceBoard.getId());

        partialUpdatedInterfaceBoard.ipAddress(UPDATED_IP_ADDRESS).hash(UPDATED_HASH).mac(UPDATED_MAC);

        restInterfaceBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInterfaceBoard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInterfaceBoard))
            )
            .andExpect(status().isOk());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeUpdate);
        InterfaceBoard testInterfaceBoard = interfaceBoardList.get(interfaceBoardList.size() - 1);
        assertThat(testInterfaceBoard.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testInterfaceBoard.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testInterfaceBoard.getMac()).isEqualTo(UPDATED_MAC);
    }

    @Test
    @Transactional
    void patchNonExistingInterfaceBoard() throws Exception {
        int databaseSizeBeforeUpdate = interfaceBoardRepository.findAll().size();
        interfaceBoard.setId(count.incrementAndGet());

        // Create the InterfaceBoard
        InterfaceBoardDTO interfaceBoardDTO = interfaceBoardMapper.toDto(interfaceBoard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterfaceBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, interfaceBoardDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interfaceBoardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInterfaceBoard() throws Exception {
        int databaseSizeBeforeUpdate = interfaceBoardRepository.findAll().size();
        interfaceBoard.setId(count.incrementAndGet());

        // Create the InterfaceBoard
        InterfaceBoardDTO interfaceBoardDTO = interfaceBoardMapper.toDto(interfaceBoard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterfaceBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interfaceBoardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInterfaceBoard() throws Exception {
        int databaseSizeBeforeUpdate = interfaceBoardRepository.findAll().size();
        interfaceBoard.setId(count.incrementAndGet());

        // Create the InterfaceBoard
        InterfaceBoardDTO interfaceBoardDTO = interfaceBoardMapper.toDto(interfaceBoard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterfaceBoardMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interfaceBoardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInterfaceBoard() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        int databaseSizeBeforeDelete = interfaceBoardRepository.findAll().size();

        // Delete the interfaceBoard
        restInterfaceBoardMockMvc
            .perform(delete(ENTITY_API_URL_ID, interfaceBoard.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
