package co.com.ies.smol.web.rest;

import co.com.ies.smol.repository.ReceptionOrderRepository;
import co.com.ies.smol.service.ReceptionOrderQueryService;
import co.com.ies.smol.service.ReceptionOrderService;
import co.com.ies.smol.service.criteria.ReceptionOrderCriteria;
import co.com.ies.smol.service.dto.ReceptionOrderDTO;
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
 * REST controller for managing {@link co.com.ies.smol.domain.ReceptionOrder}.
 */
@RestController
@RequestMapping("/api")
public class ReceptionOrderResource {

    private final Logger log = LoggerFactory.getLogger(ReceptionOrderResource.class);

    private static final String ENTITY_NAME = "receptionOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReceptionOrderService receptionOrderService;

    private final ReceptionOrderRepository receptionOrderRepository;

    private final ReceptionOrderQueryService receptionOrderQueryService;

    public ReceptionOrderResource(
        ReceptionOrderService receptionOrderService,
        ReceptionOrderRepository receptionOrderRepository,
        ReceptionOrderQueryService receptionOrderQueryService
    ) {
        this.receptionOrderService = receptionOrderService;
        this.receptionOrderRepository = receptionOrderRepository;
        this.receptionOrderQueryService = receptionOrderQueryService;
    }

    /**
     * {@code POST  /reception-orders} : Create a new receptionOrder.
     *
     * @param receptionOrderDTO the receptionOrderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new receptionOrderDTO, or with status {@code 400 (Bad Request)} if the receptionOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reception-orders")
    public ResponseEntity<ReceptionOrderDTO> createReceptionOrder(@Valid @RequestBody ReceptionOrderDTO receptionOrderDTO)
        throws URISyntaxException {
        log.debug("REST request to save ReceptionOrder : {}", receptionOrderDTO);
        if (receptionOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new receptionOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReceptionOrderDTO result = receptionOrderService.save(receptionOrderDTO);
        return ResponseEntity
            .created(new URI("/api/reception-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reception-orders/:id} : Updates an existing receptionOrder.
     *
     * @param id the id of the receptionOrderDTO to save.
     * @param receptionOrderDTO the receptionOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated receptionOrderDTO,
     * or with status {@code 400 (Bad Request)} if the receptionOrderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the receptionOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reception-orders/{id}")
    public ResponseEntity<ReceptionOrderDTO> updateReceptionOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReceptionOrderDTO receptionOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReceptionOrder : {}, {}", id, receptionOrderDTO);
        if (receptionOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, receptionOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!receptionOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReceptionOrderDTO result = receptionOrderService.update(receptionOrderDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, receptionOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reception-orders/:id} : Partial updates given fields of an existing receptionOrder, field will ignore if it is null
     *
     * @param id the id of the receptionOrderDTO to save.
     * @param receptionOrderDTO the receptionOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated receptionOrderDTO,
     * or with status {@code 400 (Bad Request)} if the receptionOrderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the receptionOrderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the receptionOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reception-orders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReceptionOrderDTO> partialUpdateReceptionOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReceptionOrderDTO receptionOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReceptionOrder partially : {}, {}", id, receptionOrderDTO);
        if (receptionOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, receptionOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!receptionOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReceptionOrderDTO> result = receptionOrderService.partialUpdate(receptionOrderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, receptionOrderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reception-orders} : get all the receptionOrders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of receptionOrders in body.
     */
    @GetMapping("/reception-orders")
    public ResponseEntity<List<ReceptionOrderDTO>> getAllReceptionOrders(
        ReceptionOrderCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ReceptionOrders by criteria: {}", criteria);
        Page<ReceptionOrderDTO> page = receptionOrderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reception-orders/count} : count all the receptionOrders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/reception-orders/count")
    public ResponseEntity<Long> countReceptionOrders(ReceptionOrderCriteria criteria) {
        log.debug("REST request to count ReceptionOrders by criteria: {}", criteria);
        return ResponseEntity.ok().body(receptionOrderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /reception-orders/:id} : get the "id" receptionOrder.
     *
     * @param id the id of the receptionOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the receptionOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reception-orders/{id}")
    public ResponseEntity<ReceptionOrderDTO> getReceptionOrder(@PathVariable Long id) {
        log.debug("REST request to get ReceptionOrder : {}", id);
        Optional<ReceptionOrderDTO> receptionOrderDTO = receptionOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(receptionOrderDTO);
    }

    /**
     * {@code DELETE  /reception-orders/:id} : delete the "id" receptionOrder.
     *
     * @param id the id of the receptionOrderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reception-orders/{id}")
    public ResponseEntity<Void> deleteReceptionOrder(@PathVariable Long id) {
        log.debug("REST request to delete ReceptionOrder : {}", id);
        receptionOrderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
