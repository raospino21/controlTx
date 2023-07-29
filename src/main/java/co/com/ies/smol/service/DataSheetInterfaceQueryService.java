package co.com.ies.smol.service;

import co.com.ies.smol.domain.*; // for static metamodels
import co.com.ies.smol.domain.DataSheetInterface;
import co.com.ies.smol.repository.DataSheetInterfaceRepository;
import co.com.ies.smol.service.criteria.DataSheetInterfaceCriteria;
import co.com.ies.smol.service.dto.DataSheetInterfaceDTO;
import co.com.ies.smol.service.mapper.DataSheetInterfaceMapper;
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
 * Service for executing complex queries for {@link DataSheetInterface} entities in the database.
 * The main input is a {@link DataSheetInterfaceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DataSheetInterfaceDTO} or a {@link Page} of {@link DataSheetInterfaceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DataSheetInterfaceQueryService extends QueryService<DataSheetInterface> {

    private final Logger log = LoggerFactory.getLogger(DataSheetInterfaceQueryService.class);

    private final DataSheetInterfaceRepository dataSheetInterfaceRepository;

    private final DataSheetInterfaceMapper dataSheetInterfaceMapper;

    public DataSheetInterfaceQueryService(
        DataSheetInterfaceRepository dataSheetInterfaceRepository,
        DataSheetInterfaceMapper dataSheetInterfaceMapper
    ) {
        this.dataSheetInterfaceRepository = dataSheetInterfaceRepository;
        this.dataSheetInterfaceMapper = dataSheetInterfaceMapper;
    }

    /**
     * Return a {@link List} of {@link DataSheetInterfaceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DataSheetInterfaceDTO> findByCriteria(DataSheetInterfaceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DataSheetInterface> specification = createSpecification(criteria);
        return dataSheetInterfaceMapper.toDto(dataSheetInterfaceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DataSheetInterfaceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DataSheetInterfaceDTO> findByCriteria(DataSheetInterfaceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DataSheetInterface> specification = createSpecification(criteria);
        return dataSheetInterfaceRepository.findAll(specification, page).map(dataSheetInterfaceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DataSheetInterfaceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DataSheetInterface> specification = createSpecification(criteria);
        return dataSheetInterfaceRepository.count(specification);
    }

    /**
     * Function to convert {@link DataSheetInterfaceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DataSheetInterface> createSpecification(DataSheetInterfaceCriteria criteria) {
        Specification<DataSheetInterface> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DataSheetInterface_.id));
            }
            if (criteria.getColcircuitosLotNumber() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getColcircuitosLotNumber(), DataSheetInterface_.colcircuitosLotNumber)
                    );
            }
            if (criteria.getOrderAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderAmount(), DataSheetInterface_.orderAmount));
            }
            if (criteria.getAmountReceived() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAmountReceived(), DataSheetInterface_.amountReceived));
            }
            if (criteria.getRemission() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRemission(), DataSheetInterface_.remission));
            }
            if (criteria.getEntryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEntryDate(), DataSheetInterface_.entryDate));
            }
            if (criteria.getIesOrderNumber() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getIesOrderNumber(), DataSheetInterface_.iesOrderNumber));
            }
        }
        return specification;
    }
}
