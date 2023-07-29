package co.com.ies.smol.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.Brand;
import co.com.ies.smol.domain.Operator;
import co.com.ies.smol.repository.OperatorRepository;
import co.com.ies.smol.service.OperatorService;
import co.com.ies.smol.service.criteria.OperatorCriteria;
import co.com.ies.smol.service.dto.OperatorDTO;
import co.com.ies.smol.service.mapper.OperatorMapper;
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
 * Integration tests for the {@link OperatorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OperatorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NIT = "AAAAAAAAAA";
    private static final String UPDATED_NIT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/operators";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OperatorRepository operatorRepository;

    @Mock
    private OperatorRepository operatorRepositoryMock;

    @Autowired
    private OperatorMapper operatorMapper;

    @Mock
    private OperatorService operatorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOperatorMockMvc;

    private Operator operator;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operator createEntity(EntityManager em) {
        Operator operator = new Operator().name(DEFAULT_NAME).nit(DEFAULT_NIT);
        // Add required entity
        Brand brand;
        if (TestUtil.findAll(em, Brand.class).isEmpty()) {
            brand = BrandResourceIT.createEntity(em);
            em.persist(brand);
            em.flush();
        } else {
            brand = TestUtil.findAll(em, Brand.class).get(0);
        }
        operator.setBrand(brand);
        return operator;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operator createUpdatedEntity(EntityManager em) {
        Operator operator = new Operator().name(UPDATED_NAME).nit(UPDATED_NIT);
        // Add required entity
        Brand brand;
        if (TestUtil.findAll(em, Brand.class).isEmpty()) {
            brand = BrandResourceIT.createUpdatedEntity(em);
            em.persist(brand);
            em.flush();
        } else {
            brand = TestUtil.findAll(em, Brand.class).get(0);
        }
        operator.setBrand(brand);
        return operator;
    }

    @BeforeEach
    public void initTest() {
        operator = createEntity(em);
    }

    @Test
    @Transactional
    void createOperator() throws Exception {
        int databaseSizeBeforeCreate = operatorRepository.findAll().size();
        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);
        restOperatorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatorDTO)))
            .andExpect(status().isCreated());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeCreate + 1);
        Operator testOperator = operatorList.get(operatorList.size() - 1);
        assertThat(testOperator.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOperator.getNit()).isEqualTo(DEFAULT_NIT);
    }

    @Test
    @Transactional
    void createOperatorWithExistingId() throws Exception {
        // Create the Operator with an existing ID
        operator.setId(1L);
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        int databaseSizeBeforeCreate = operatorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperatorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOperators() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operator.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nit").value(hasItem(DEFAULT_NIT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOperatorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(operatorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOperatorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(operatorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOperatorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(operatorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOperatorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(operatorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOperator() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get the operator
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL_ID, operator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operator.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.nit").value(DEFAULT_NIT));
    }

    @Test
    @Transactional
    void getOperatorsByIdFiltering() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        Long id = operator.getId();

        defaultOperatorShouldBeFound("id.equals=" + id);
        defaultOperatorShouldNotBeFound("id.notEquals=" + id);

        defaultOperatorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOperatorShouldNotBeFound("id.greaterThan=" + id);

        defaultOperatorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOperatorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOperatorsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where name equals to DEFAULT_NAME
        defaultOperatorShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the operatorList where name equals to UPDATED_NAME
        defaultOperatorShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOperatorsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOperatorShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the operatorList where name equals to UPDATED_NAME
        defaultOperatorShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOperatorsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where name is not null
        defaultOperatorShouldBeFound("name.specified=true");

        // Get all the operatorList where name is null
        defaultOperatorShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllOperatorsByNameContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where name contains DEFAULT_NAME
        defaultOperatorShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the operatorList where name contains UPDATED_NAME
        defaultOperatorShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOperatorsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where name does not contain DEFAULT_NAME
        defaultOperatorShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the operatorList where name does not contain UPDATED_NAME
        defaultOperatorShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOperatorsByNitIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nit equals to DEFAULT_NIT
        defaultOperatorShouldBeFound("nit.equals=" + DEFAULT_NIT);

        // Get all the operatorList where nit equals to UPDATED_NIT
        defaultOperatorShouldNotBeFound("nit.equals=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllOperatorsByNitIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nit in DEFAULT_NIT or UPDATED_NIT
        defaultOperatorShouldBeFound("nit.in=" + DEFAULT_NIT + "," + UPDATED_NIT);

        // Get all the operatorList where nit equals to UPDATED_NIT
        defaultOperatorShouldNotBeFound("nit.in=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllOperatorsByNitIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nit is not null
        defaultOperatorShouldBeFound("nit.specified=true");

        // Get all the operatorList where nit is null
        defaultOperatorShouldNotBeFound("nit.specified=false");
    }

    @Test
    @Transactional
    void getAllOperatorsByNitContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nit contains DEFAULT_NIT
        defaultOperatorShouldBeFound("nit.contains=" + DEFAULT_NIT);

        // Get all the operatorList where nit contains UPDATED_NIT
        defaultOperatorShouldNotBeFound("nit.contains=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllOperatorsByNitNotContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nit does not contain DEFAULT_NIT
        defaultOperatorShouldNotBeFound("nit.doesNotContain=" + DEFAULT_NIT);

        // Get all the operatorList where nit does not contain UPDATED_NIT
        defaultOperatorShouldBeFound("nit.doesNotContain=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllOperatorsByBrandIsEqualToSomething() throws Exception {
        Brand brand;
        if (TestUtil.findAll(em, Brand.class).isEmpty()) {
            operatorRepository.saveAndFlush(operator);
            brand = BrandResourceIT.createEntity(em);
        } else {
            brand = TestUtil.findAll(em, Brand.class).get(0);
        }
        em.persist(brand);
        em.flush();
        operator.setBrand(brand);
        operatorRepository.saveAndFlush(operator);
        Long brandId = brand.getId();

        // Get all the operatorList where brand equals to brandId
        defaultOperatorShouldBeFound("brandId.equals=" + brandId);

        // Get all the operatorList where brand equals to (brandId + 1)
        defaultOperatorShouldNotBeFound("brandId.equals=" + (brandId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOperatorShouldBeFound(String filter) throws Exception {
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operator.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nit").value(hasItem(DEFAULT_NIT)));

        // Check, that the count call also returns 1
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOperatorShouldNotBeFound(String filter) throws Exception {
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOperator() throws Exception {
        // Get the operator
        restOperatorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOperator() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();

        // Update the operator
        Operator updatedOperator = operatorRepository.findById(operator.getId()).get();
        // Disconnect from session so that the updates on updatedOperator are not directly saved in db
        em.detach(updatedOperator);
        updatedOperator.name(UPDATED_NAME).nit(UPDATED_NIT);
        OperatorDTO operatorDTO = operatorMapper.toDto(updatedOperator);

        restOperatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operatorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operatorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
        Operator testOperator = operatorList.get(operatorList.size() - 1);
        assertThat(testOperator.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOperator.getNit()).isEqualTo(UPDATED_NIT);
    }

    @Test
    @Transactional
    void putNonExistingOperator() throws Exception {
        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();
        operator.setId(count.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operatorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOperator() throws Exception {
        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();
        operator.setId(count.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOperator() throws Exception {
        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();
        operator.setId(count.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOperatorWithPatch() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();

        // Update the operator using partial update
        Operator partialUpdatedOperator = new Operator();
        partialUpdatedOperator.setId(operator.getId());

        restOperatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperator))
            )
            .andExpect(status().isOk());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
        Operator testOperator = operatorList.get(operatorList.size() - 1);
        assertThat(testOperator.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOperator.getNit()).isEqualTo(DEFAULT_NIT);
    }

    @Test
    @Transactional
    void fullUpdateOperatorWithPatch() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();

        // Update the operator using partial update
        Operator partialUpdatedOperator = new Operator();
        partialUpdatedOperator.setId(operator.getId());

        partialUpdatedOperator.name(UPDATED_NAME).nit(UPDATED_NIT);

        restOperatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperator))
            )
            .andExpect(status().isOk());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
        Operator testOperator = operatorList.get(operatorList.size() - 1);
        assertThat(testOperator.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOperator.getNit()).isEqualTo(UPDATED_NIT);
    }

    @Test
    @Transactional
    void patchNonExistingOperator() throws Exception {
        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();
        operator.setId(count.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, operatorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOperator() throws Exception {
        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();
        operator.setId(count.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOperator() throws Exception {
        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();
        operator.setId(count.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(operatorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOperator() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        int databaseSizeBeforeDelete = operatorRepository.findAll().size();

        // Delete the operator
        restOperatorMockMvc
            .perform(delete(ENTITY_API_URL_ID, operator.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
