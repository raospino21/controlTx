package co.com.ies.smol.service;

import co.com.ies.smol.domain.*; // for static metamodels
import co.com.ies.smol.repository.InterfaceBoardRepository;
import co.com.ies.smol.service.criteria.InterfaceBoardCriteria;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import co.com.ies.smol.service.mapper.InterfaceBoardMapper;
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
 * Service for executing complex queries for {@link InterfaceBoard} entities in the database.
 * The main input is a {@link InterfaceBoardCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InterfaceBoardDTO} or a {@link Page} of {@link InterfaceBoardDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InterfaceBoardQueryService extends QueryService<InterfaceBoard> {

    private final Logger log = LoggerFactory.getLogger(InterfaceBoardQueryService.class);

    private final InterfaceBoardRepository interfaceBoardRepository;

    private final InterfaceBoardMapper interfaceBoardMapper;

    public InterfaceBoardQueryService(InterfaceBoardRepository interfaceBoardRepository, InterfaceBoardMapper interfaceBoardMapper) {
        this.interfaceBoardRepository = interfaceBoardRepository;
        this.interfaceBoardMapper = interfaceBoardMapper;
    }

    /**
     * Return a {@link List} of {@link InterfaceBoardDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InterfaceBoardDTO> findByCriteria(InterfaceBoardCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InterfaceBoard> specification = createSpecification(criteria);
        return interfaceBoardMapper.toDto(interfaceBoardRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InterfaceBoardDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InterfaceBoardDTO> findByCriteria(InterfaceBoardCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InterfaceBoard> specification = createSpecification(criteria);
        return interfaceBoardRepository.findAll(specification, page).map(interfaceBoardMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InterfaceBoardCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InterfaceBoard> specification = createSpecification(criteria);
        return interfaceBoardRepository.count(specification);
    }

    /**
     * Function to convert {@link InterfaceBoardCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InterfaceBoard> createSpecification(InterfaceBoardCriteria criteria) {
        Specification<InterfaceBoard> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InterfaceBoard_.id));
            }
            if (criteria.getIsValidated() != null) {
                specification = specification.and(buildSpecification(criteria.getIsValidated(), InterfaceBoard_.isValidated));
            }
            if (criteria.getIpAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIpAddress(), InterfaceBoard_.ipAddress));
            }
            if (criteria.getHash() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHash(), InterfaceBoard_.hash));
            }
            if (criteria.getMac() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMac(), InterfaceBoard_.mac));
            }
            if (criteria.getReceptionOrderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReceptionOrderId(),
                            root -> root.join(InterfaceBoard_.receptionOrder, JoinType.LEFT).get(ReceptionOrder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
