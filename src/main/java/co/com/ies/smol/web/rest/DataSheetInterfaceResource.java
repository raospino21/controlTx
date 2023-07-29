package co.com.ies.smol.web.rest;

import co.com.ies.smol.repository.DataSheetInterfaceRepository;
import co.com.ies.smol.service.DataSheetInterfaceQueryService;
import co.com.ies.smol.service.DataSheetInterfaceService;
import co.com.ies.smol.service.criteria.DataSheetInterfaceCriteria;
import co.com.ies.smol.service.dto.DataSheetInterfaceDTO;
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
 * REST controller for managing {@link co.com.ies.smol.domain.DataSheetInterface}.
 */
@RestController
@RequestMapping("/api")
public class DataSheetInterfaceResource {

    private final Logger log = LoggerFactory.getLogger(DataSheetInterfaceResource.class);

    private static final String ENTITY_NAME = "dataSheetInterface";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DataSheetInterfaceService dataSheetInterfaceService;

    private final DataSheetInterfaceRepository dataSheetInterfaceRepository;

    private final DataSheetInterfaceQueryService dataSheetInterfaceQueryService;

    public DataSheetInterfaceResource(
        DataSheetInterfaceService dataSheetInterfaceService,
        DataSheetInterfaceRepository dataSheetInterfaceRepository,
        DataSheetInterfaceQueryService dataSheetInterfaceQueryService
    ) {
        this.dataSheetInterfaceService = dataSheetInterfaceService;
        this.dataSheetInterfaceRepository = dataSheetInterfaceRepository;
        this.dataSheetInterfaceQueryService = dataSheetInterfaceQueryService;
    }

    /**
     * {@code POST  /data-sheet-interfaces} : Create a new dataSheetInterface.
     *
     * @param dataSheetInterfaceDTO the dataSheetInterfaceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dataSheetInterfaceDTO, or with status {@code 400 (Bad Request)} if the dataSheetInterface has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/data-sheet-interfaces")
    public ResponseEntity<DataSheetInterfaceDTO> createDataSheetInterface(@Valid @RequestBody DataSheetInterfaceDTO dataSheetInterfaceDTO)
        throws URISyntaxException {
        log.debug("REST request to save DataSheetInterface : {}", dataSheetInterfaceDTO);
        if (dataSheetInterfaceDTO.getId() != null) {
            throw new BadRequestAlertException("A new dataSheetInterface cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DataSheetInterfaceDTO result = dataSheetInterfaceService.save(dataSheetInterfaceDTO);
        return ResponseEntity
            .created(new URI("/api/data-sheet-interfaces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /data-sheet-interfaces/:id} : Updates an existing dataSheetInterface.
     *
     * @param id the id of the dataSheetInterfaceDTO to save.
     * @param dataSheetInterfaceDTO the dataSheetInterfaceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataSheetInterfaceDTO,
     * or with status {@code 400 (Bad Request)} if the dataSheetInterfaceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dataSheetInterfaceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/data-sheet-interfaces/{id}")
    public ResponseEntity<DataSheetInterfaceDTO> updateDataSheetInterface(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DataSheetInterfaceDTO dataSheetInterfaceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DataSheetInterface : {}, {}", id, dataSheetInterfaceDTO);
        if (dataSheetInterfaceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataSheetInterfaceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataSheetInterfaceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DataSheetInterfaceDTO result = dataSheetInterfaceService.update(dataSheetInterfaceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dataSheetInterfaceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /data-sheet-interfaces/:id} : Partial updates given fields of an existing dataSheetInterface, field will ignore if it is null
     *
     * @param id the id of the dataSheetInterfaceDTO to save.
     * @param dataSheetInterfaceDTO the dataSheetInterfaceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataSheetInterfaceDTO,
     * or with status {@code 400 (Bad Request)} if the dataSheetInterfaceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dataSheetInterfaceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dataSheetInterfaceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/data-sheet-interfaces/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DataSheetInterfaceDTO> partialUpdateDataSheetInterface(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DataSheetInterfaceDTO dataSheetInterfaceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DataSheetInterface partially : {}, {}", id, dataSheetInterfaceDTO);
        if (dataSheetInterfaceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataSheetInterfaceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataSheetInterfaceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DataSheetInterfaceDTO> result = dataSheetInterfaceService.partialUpdate(dataSheetInterfaceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dataSheetInterfaceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /data-sheet-interfaces} : get all the dataSheetInterfaces.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dataSheetInterfaces in body.
     */
    @GetMapping("/data-sheet-interfaces")
    public ResponseEntity<List<DataSheetInterfaceDTO>> getAllDataSheetInterfaces(
        DataSheetInterfaceCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DataSheetInterfaces by criteria: {}", criteria);
        Page<DataSheetInterfaceDTO> page = dataSheetInterfaceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /data-sheet-interfaces/count} : count all the dataSheetInterfaces.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/data-sheet-interfaces/count")
    public ResponseEntity<Long> countDataSheetInterfaces(DataSheetInterfaceCriteria criteria) {
        log.debug("REST request to count DataSheetInterfaces by criteria: {}", criteria);
        return ResponseEntity.ok().body(dataSheetInterfaceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /data-sheet-interfaces/:id} : get the "id" dataSheetInterface.
     *
     * @param id the id of the dataSheetInterfaceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dataSheetInterfaceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/data-sheet-interfaces/{id}")
    public ResponseEntity<DataSheetInterfaceDTO> getDataSheetInterface(@PathVariable Long id) {
        log.debug("REST request to get DataSheetInterface : {}", id);
        Optional<DataSheetInterfaceDTO> dataSheetInterfaceDTO = dataSheetInterfaceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dataSheetInterfaceDTO);
    }

    /**
     * {@code DELETE  /data-sheet-interfaces/:id} : delete the "id" dataSheetInterface.
     *
     * @param id the id of the dataSheetInterfaceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/data-sheet-interfaces/{id}")
    public ResponseEntity<Void> deleteDataSheetInterface(@PathVariable Long id) {
        log.debug("REST request to delete DataSheetInterface : {}", id);
        dataSheetInterfaceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
