package co.com.ies.smol.web.rest;

import co.com.ies.smol.repository.ControlInterfaceBoardRepository;
import co.com.ies.smol.service.ControlInterfaceBoardQueryService;
import co.com.ies.smol.service.ControlInterfaceBoardService;
import co.com.ies.smol.service.criteria.ControlInterfaceBoardCriteria;
import co.com.ies.smol.service.dto.ControlInterfaceBoardDTO;
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
 * REST controller for managing {@link co.com.ies.smol.domain.ControlInterfaceBoard}.
 */
@RestController
@RequestMapping("/api")
public class ControlInterfaceBoardResource {

    private final Logger log = LoggerFactory.getLogger(ControlInterfaceBoardResource.class);

    private static final String ENTITY_NAME = "controlInterfaceBoard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ControlInterfaceBoardService controlInterfaceBoardService;

    private final ControlInterfaceBoardRepository controlInterfaceBoardRepository;

    private final ControlInterfaceBoardQueryService controlInterfaceBoardQueryService;

    public ControlInterfaceBoardResource(
        ControlInterfaceBoardService controlInterfaceBoardService,
        ControlInterfaceBoardRepository controlInterfaceBoardRepository,
        ControlInterfaceBoardQueryService controlInterfaceBoardQueryService
    ) {
        this.controlInterfaceBoardService = controlInterfaceBoardService;
        this.controlInterfaceBoardRepository = controlInterfaceBoardRepository;
        this.controlInterfaceBoardQueryService = controlInterfaceBoardQueryService;
    }

    /**
     * {@code POST  /control-interface-boards} : Create a new controlInterfaceBoard.
     *
     * @param controlInterfaceBoardDTO the controlInterfaceBoardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new controlInterfaceBoardDTO, or with status {@code 400 (Bad Request)} if the controlInterfaceBoard has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/control-interface-boards")
    public ResponseEntity<ControlInterfaceBoardDTO> createControlInterfaceBoard(
        @Valid @RequestBody ControlInterfaceBoardDTO controlInterfaceBoardDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ControlInterfaceBoard : {}", controlInterfaceBoardDTO);
        if (controlInterfaceBoardDTO.getId() != null) {
            throw new BadRequestAlertException("A new controlInterfaceBoard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ControlInterfaceBoardDTO result = controlInterfaceBoardService.save(controlInterfaceBoardDTO);
        return ResponseEntity
            .created(new URI("/api/control-interface-boards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /control-interface-boards/:id} : Updates an existing controlInterfaceBoard.
     *
     * @param id the id of the controlInterfaceBoardDTO to save.
     * @param controlInterfaceBoardDTO the controlInterfaceBoardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated controlInterfaceBoardDTO,
     * or with status {@code 400 (Bad Request)} if the controlInterfaceBoardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the controlInterfaceBoardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/control-interface-boards/{id}")
    public ResponseEntity<ControlInterfaceBoardDTO> updateControlInterfaceBoard(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ControlInterfaceBoardDTO controlInterfaceBoardDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ControlInterfaceBoard : {}, {}", id, controlInterfaceBoardDTO);
        if (controlInterfaceBoardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, controlInterfaceBoardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!controlInterfaceBoardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ControlInterfaceBoardDTO result = controlInterfaceBoardService.update(controlInterfaceBoardDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, controlInterfaceBoardDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /control-interface-boards/:id} : Partial updates given fields of an existing controlInterfaceBoard, field will ignore if it is null
     *
     * @param id the id of the controlInterfaceBoardDTO to save.
     * @param controlInterfaceBoardDTO the controlInterfaceBoardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated controlInterfaceBoardDTO,
     * or with status {@code 400 (Bad Request)} if the controlInterfaceBoardDTO is not valid,
     * or with status {@code 404 (Not Found)} if the controlInterfaceBoardDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the controlInterfaceBoardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/control-interface-boards/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ControlInterfaceBoardDTO> partialUpdateControlInterfaceBoard(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ControlInterfaceBoardDTO controlInterfaceBoardDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ControlInterfaceBoard partially : {}, {}", id, controlInterfaceBoardDTO);
        if (controlInterfaceBoardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, controlInterfaceBoardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!controlInterfaceBoardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ControlInterfaceBoardDTO> result = controlInterfaceBoardService.partialUpdate(controlInterfaceBoardDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, controlInterfaceBoardDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /control-interface-boards} : get all the controlInterfaceBoards.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of controlInterfaceBoards in body.
     */
    @GetMapping("/control-interface-boards")
    public ResponseEntity<List<ControlInterfaceBoardDTO>> getAllControlInterfaceBoards(
        ControlInterfaceBoardCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ControlInterfaceBoards by criteria: {}", criteria);
        Page<ControlInterfaceBoardDTO> page = controlInterfaceBoardQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /control-interface-boards/count} : count all the controlInterfaceBoards.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/control-interface-boards/count")
    public ResponseEntity<Long> countControlInterfaceBoards(ControlInterfaceBoardCriteria criteria) {
        log.debug("REST request to count ControlInterfaceBoards by criteria: {}", criteria);
        return ResponseEntity.ok().body(controlInterfaceBoardQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /control-interface-boards/:id} : get the "id" controlInterfaceBoard.
     *
     * @param id the id of the controlInterfaceBoardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the controlInterfaceBoardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/control-interface-boards/{id}")
    public ResponseEntity<ControlInterfaceBoardDTO> getControlInterfaceBoard(@PathVariable Long id) {
        log.debug("REST request to get ControlInterfaceBoard : {}", id);
        Optional<ControlInterfaceBoardDTO> controlInterfaceBoardDTO = controlInterfaceBoardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(controlInterfaceBoardDTO);
    }

    /**
     * {@code DELETE  /control-interface-boards/:id} : delete the "id" controlInterfaceBoard.
     *
     * @param id the id of the controlInterfaceBoardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/control-interface-boards/{id}")
    public ResponseEntity<Void> deleteControlInterfaceBoard(@PathVariable Long id) {
        log.debug("REST request to delete ControlInterfaceBoard : {}", id);
        controlInterfaceBoardService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
