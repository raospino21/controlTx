package co.com.ies.smol.web.rest;

import co.com.ies.smol.repository.InterfaceBoardRepository;
import co.com.ies.smol.service.InterfaceBoardQueryService;
import co.com.ies.smol.service.InterfaceBoardService;
import co.com.ies.smol.service.criteria.InterfaceBoardCriteria;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
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
 * REST controller for managing {@link co.com.ies.smol.domain.InterfaceBoard}.
 */
@RestController
@RequestMapping("/api")
public class InterfaceBoardResource {

    private final Logger log = LoggerFactory.getLogger(InterfaceBoardResource.class);

    private static final String ENTITY_NAME = "interfaceBoard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InterfaceBoardService interfaceBoardService;

    private final InterfaceBoardRepository interfaceBoardRepository;

    private final InterfaceBoardQueryService interfaceBoardQueryService;

    public InterfaceBoardResource(
        InterfaceBoardService interfaceBoardService,
        InterfaceBoardRepository interfaceBoardRepository,
        InterfaceBoardQueryService interfaceBoardQueryService
    ) {
        this.interfaceBoardService = interfaceBoardService;
        this.interfaceBoardRepository = interfaceBoardRepository;
        this.interfaceBoardQueryService = interfaceBoardQueryService;
    }

    /**
     * {@code POST  /interface-boards} : Create a new interfaceBoard.
     *
     * @param interfaceBoardDTO the interfaceBoardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new interfaceBoardDTO, or with status {@code 400 (Bad Request)} if the interfaceBoard has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/interface-boards")
    public ResponseEntity<InterfaceBoardDTO> createInterfaceBoard(@Valid @RequestBody InterfaceBoardDTO interfaceBoardDTO)
        throws URISyntaxException {
        log.debug("REST request to save InterfaceBoard : {}", interfaceBoardDTO);
        if (interfaceBoardDTO.getId() != null) {
            throw new BadRequestAlertException("A new interfaceBoard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InterfaceBoardDTO result = interfaceBoardService.save(interfaceBoardDTO);
        return ResponseEntity
            .created(new URI("/api/interface-boards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /interface-boards/:id} : Updates an existing interfaceBoard.
     *
     * @param id the id of the interfaceBoardDTO to save.
     * @param interfaceBoardDTO the interfaceBoardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interfaceBoardDTO,
     * or with status {@code 400 (Bad Request)} if the interfaceBoardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the interfaceBoardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/interface-boards/{id}")
    public ResponseEntity<InterfaceBoardDTO> updateInterfaceBoard(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InterfaceBoardDTO interfaceBoardDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InterfaceBoard : {}, {}", id, interfaceBoardDTO);
        if (interfaceBoardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, interfaceBoardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interfaceBoardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InterfaceBoardDTO result = interfaceBoardService.update(interfaceBoardDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, interfaceBoardDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /interface-boards/:id} : Partial updates given fields of an existing interfaceBoard, field will ignore if it is null
     *
     * @param id the id of the interfaceBoardDTO to save.
     * @param interfaceBoardDTO the interfaceBoardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interfaceBoardDTO,
     * or with status {@code 400 (Bad Request)} if the interfaceBoardDTO is not valid,
     * or with status {@code 404 (Not Found)} if the interfaceBoardDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the interfaceBoardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/interface-boards/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InterfaceBoardDTO> partialUpdateInterfaceBoard(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InterfaceBoardDTO interfaceBoardDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InterfaceBoard partially : {}, {}", id, interfaceBoardDTO);
        if (interfaceBoardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, interfaceBoardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interfaceBoardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InterfaceBoardDTO> result = interfaceBoardService.partialUpdate(interfaceBoardDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, interfaceBoardDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /interface-boards} : get all the interfaceBoards.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of interfaceBoards in body.
     */
    @GetMapping("/interface-boards")
    public ResponseEntity<List<InterfaceBoardDTO>> getAllInterfaceBoards(
        InterfaceBoardCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get InterfaceBoards by criteria: {}", criteria);
        Page<InterfaceBoardDTO> page = interfaceBoardQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /interface-boards/count} : count all the interfaceBoards.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/interface-boards/count")
    public ResponseEntity<Long> countInterfaceBoards(InterfaceBoardCriteria criteria) {
        log.debug("REST request to count InterfaceBoards by criteria: {}", criteria);
        return ResponseEntity.ok().body(interfaceBoardQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /interface-boards/:id} : get the "id" interfaceBoard.
     *
     * @param id the id of the interfaceBoardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the interfaceBoardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/interface-boards/{id}")
    public ResponseEntity<InterfaceBoardDTO> getInterfaceBoard(@PathVariable Long id) {
        log.debug("REST request to get InterfaceBoard : {}", id);
        Optional<InterfaceBoardDTO> interfaceBoardDTO = interfaceBoardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(interfaceBoardDTO);
    }

    /**
     * {@code DELETE  /interface-boards/:id} : delete the "id" interfaceBoard.
     *
     * @param id the id of the interfaceBoardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/interface-boards/{id}")
    public ResponseEntity<Void> deleteInterfaceBoard(@PathVariable Long id) {
        log.debug("REST request to delete InterfaceBoard : {}", id);
        interfaceBoardService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
