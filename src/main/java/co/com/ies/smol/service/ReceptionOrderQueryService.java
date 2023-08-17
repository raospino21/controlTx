package co.com.ies.smol.service;

import co.com.ies.smol.domain.*; // for static metamodels
import co.com.ies.smol.domain.ReceptionOrder;
import co.com.ies.smol.repository.ReceptionOrderRepository;
import co.com.ies.smol.service.criteria.ReceptionOrderCriteria;
import co.com.ies.smol.service.dto.ReceptionOrderDTO;
import co.com.ies.smol.service.mapper.ReceptionOrderMapper;
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
 * Service for executing complex queries for {@link ReceptionOrder} entities in the database.
 * The main input is a {@link ReceptionOrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReceptionOrderDTO} or a {@link Page} of {@link ReceptionOrderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReceptionOrderQueryService extends QueryService<ReceptionOrder> {

    private final Logger log = LoggerFactory.getLogger(ReceptionOrderQueryService.class);

    private final ReceptionOrderRepository receptionOrderRepository;

    private final ReceptionOrderMapper receptionOrderMapper;

    public ReceptionOrderQueryService(ReceptionOrderRepository receptionOrderRepository, ReceptionOrderMapper receptionOrderMapper) {
        this.receptionOrderRepository = receptionOrderRepository;
        this.receptionOrderMapper = receptionOrderMapper;
    }

    /**
     * Return a {@link List} of {@link ReceptionOrderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReceptionOrderDTO> findByCriteria(ReceptionOrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ReceptionOrder> specification = createSpecification(criteria);
        return receptionOrderMapper.toDto(receptionOrderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReceptionOrderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReceptionOrderDTO> findByCriteria(ReceptionOrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReceptionOrder> specification = createSpecification(criteria);
        return receptionOrderRepository.findAll(specification, page).map(receptionOrderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReceptionOrderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ReceptionOrder> specification = createSpecification(criteria);
        return receptionOrderRepository.count(specification);
    }

    /**
     * Function to convert {@link ReceptionOrderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReceptionOrder> createSpecification(ReceptionOrderCriteria criteria) {
        Specification<ReceptionOrder> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ReceptionOrder_.id));
            }
            if (criteria.getProviderLotNumber() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getProviderLotNumber(), ReceptionOrder_.providerLotNumber));
            }
            if (criteria.getAmountReceived() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmountReceived(), ReceptionOrder_.amountReceived));
            }
            if (criteria.getRemission() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRemission(), ReceptionOrder_.remission));
            }
            if (criteria.getEntryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEntryDate(), ReceptionOrder_.entryDate));
            }
            if (criteria.getWarrantyDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWarrantyDate(), ReceptionOrder_.warrantyDate));
            }
            if (criteria.getPurchaseOrderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPurchaseOrderId(),
                            root -> root.join(ReceptionOrder_.purchaseOrder, JoinType.LEFT).get(PurchaseOrder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
