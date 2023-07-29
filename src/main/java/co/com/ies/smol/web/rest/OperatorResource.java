package co.com.ies.smol.web.rest;

import co.com.ies.smol.repository.OperatorRepository;
import co.com.ies.smol.service.OperatorQueryService;
import co.com.ies.smol.service.OperatorService;
import co.com.ies.smol.service.criteria.OperatorCriteria;
import co.com.ies.smol.service.dto.OperatorDTO;
import co.com.ies.smol.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link co.com.ies.smol.domain.Operator}.
 */
@RestController
@RequestMapping("/api")
public class OperatorResource {

    private final Logger log = LoggerFactory.getLogger(OperatorResource.class);

    private static final String ENTITY_NAME = "operator";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OperatorService operatorService;

    private final OperatorRepository operatorRepository;

    private final OperatorQueryService operatorQueryService;

    public OperatorResource(
        OperatorService operatorService,
        OperatorRepository operatorRepository,
        OperatorQueryService operatorQueryService
    ) {
        this.operatorService = operatorService;
        this.operatorRepository = operatorRepository;
        this.operatorQueryService = operatorQueryService;
    }

    /**
     * {@code POST  /operators} : Create a new operator.
     *
     * @param operatorDTO the operatorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new operatorDTO, or with status {@code 400 (Bad Request)} if the operator has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/operators")
    public ResponseEntity<OperatorDTO> createOperator(@Valid @RequestBody OperatorDTO operatorDTO) throws URISyntaxException {
        log.debug("REST request to save Operator : {}", operatorDTO);
        if (operatorDTO.getId() != null) {
            throw new BadRequestAlertException("A new operator cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OperatorDTO result = operatorService.save(operatorDTO);
        return ResponseEntity
            .created(new URI("/api/operators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /operators/:id} : Updates an existing operator.
     *
     * @param id the id of the operatorDTO to save.
     * @param operatorDTO the operatorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operatorDTO,
     * or with status {@code 400 (Bad Request)} if the operatorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the operatorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/operators/{id}")
    public ResponseEntity<OperatorDTO> updateOperator(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OperatorDTO operatorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Operator : {}, {}", id, operatorDTO);
        if (operatorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, operatorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!operatorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OperatorDTO result = operatorService.update(operatorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, operatorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /operators/:id} : Partial updates given fields of an existing operator, field will ignore if it is null
     *
     * @param id the id of the operatorDTO to save.
     * @param operatorDTO the operatorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operatorDTO,
     * or with status {@code 400 (Bad Request)} if the operatorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the operatorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the operatorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/operators/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OperatorDTO> partialUpdateOperator(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OperatorDTO operatorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Operator partially : {}, {}", id, operatorDTO);
        if (operatorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, operatorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!operatorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OperatorDTO> result = operatorService.partialUpdate(operatorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, operatorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /operators} : get all the operators.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of operators in body.
     */
    @GetMapping("/operators")
    public ResponseEntity<List<OperatorDTO>> getAllOperators(
        OperatorCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Operators by criteria: {}", criteria);
        Page<OperatorDTO> page = operatorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /operators/count} : count all the operators.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/operators/count")
    public ResponseEntity<Long> countOperators(OperatorCriteria criteria) {
        log.debug("REST request to count Operators by criteria: {}", criteria);
        return ResponseEntity.ok().body(operatorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /operators/:id} : get the "id" operator.
     *
     * @param id the id of the operatorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the operatorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/operators/{id}")
    public ResponseEntity<OperatorDTO> getOperator(@PathVariable Long id) {
        log.debug("REST request to get Operator : {}", id);
        Optional<OperatorDTO> operatorDTO = operatorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(operatorDTO);
    }

    /**
     * {@code DELETE  /operators/:id} : delete the "id" operator.
     *
     * @param id the id of the operatorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/operators/{id}")
    public ResponseEntity<Void> deleteOperator(@PathVariable Long id) {
        log.debug("REST request to delete Operator : {}", id);
        operatorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
