package co.com.ies.smol.service;

import co.com.ies.smol.domain.*; // for static metamodels
import co.com.ies.smol.domain.ControlInterfaceBoard;
import co.com.ies.smol.repository.ControlInterfaceBoardRepository;
import co.com.ies.smol.service.criteria.ControlInterfaceBoardCriteria;
import co.com.ies.smol.service.dto.ControlInterfaceBoardDTO;
import co.com.ies.smol.service.mapper.ControlInterfaceBoardMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ControlInterfaceBoard} entities in the database.
 * The main input is a {@link ControlInterfaceBoardCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ControlInterfaceBoardDTO} or a {@link Page} of {@link ControlInterfaceBoardDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ControlInterfaceBoardQueryService extends QueryService<ControlInterfaceBoard> {

    private final Logger log = LoggerFactory.getLogger(ControlInterfaceBoardQueryService.class);

    private final ControlInterfaceBoardRepository controlInterfaceBoardRepository;

    private final ControlInterfaceBoardMapper controlInterfaceBoardMapper;

    public ControlInterfaceBoardQueryService(
        ControlInterfaceBoardRepository controlInterfaceBoardRepository,
        ControlInterfaceBoardMapper controlInterfaceBoardMapper
    ) {
        this.controlInterfaceBoardRepository = controlInterfaceBoardRepository;
        this.controlInterfaceBoardMapper = controlInterfaceBoardMapper;
    }

    /**
     * Return a {@link List} of {@link ControlInterfaceBoardDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ControlInterfaceBoardDTO> findByCriteria(ControlInterfaceBoardCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ControlInterfaceBoard> specification = createSpecification(criteria);
        return controlInterfaceBoardMapper.toDto(controlInterfaceBoardRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ControlInterfaceBoardDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ControlInterfaceBoardDTO> findByCriteria(ControlInterfaceBoardCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ControlInterfaceBoard> specification = createSpecification(criteria);
        return controlInterfaceBoardRepository.findAll(specification, page).map(controlInterfaceBoardMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ControlInterfaceBoardCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ControlInterfaceBoard> specification = createSpecification(criteria);
        return controlInterfaceBoardRepository.count(specification);
    }

    /**
     * Function to convert {@link ControlInterfaceBoardCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ControlInterfaceBoard> createSpecification(ControlInterfaceBoardCriteria criteria) {
        Specification<ControlInterfaceBoard> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ControlInterfaceBoard_.id));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildSpecification(criteria.getLocation(), ControlInterfaceBoard_.location));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildSpecification(criteria.getState(), ControlInterfaceBoard_.state));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), ControlInterfaceBoard_.startTime));
            }
            if (criteria.getFinishTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFinishTime(), ControlInterfaceBoard_.finishTime));
            }
            if (criteria.getContractId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContractId(),
                            root -> root.join(ControlInterfaceBoard_.contract, JoinType.LEFT).get(Contract_.id)
                        )
                    );
            }
            if (criteria.getInterfaceBoardId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInterfaceBoardId(),
                            root -> root.join(ControlInterfaceBoard_.interfaceBoard, JoinType.LEFT).get(InterfaceBoard_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
