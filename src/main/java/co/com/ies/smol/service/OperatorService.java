package co.com.ies.smol.service;

import co.com.ies.smol.domain.Operator;
import co.com.ies.smol.service.criteria.OperatorCriteria;
import co.com.ies.smol.service.dto.OperatorDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.smol.domain.Operator}.
 */
public interface OperatorService {
    /**
     * Save a operator.
     *
     * @param operatorDTO the entity to save.
     * @return the persisted entity.
     */
    OperatorDTO save(OperatorDTO operatorDTO);

    /**
     * Updates a operator.
     *
     * @param operatorDTO the entity to update.
     * @return the persisted entity.
     */
    OperatorDTO update(OperatorDTO operatorDTO);

    /**
     * Partially updates a operator.
     *
     * @param operatorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OperatorDTO> partialUpdate(OperatorDTO operatorDTO);

    /**
     * Get all the operators.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OperatorDTO> findAll(Pageable pageable);

    /**
     * Get all the operators with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OperatorDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" operator.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OperatorDTO> findOne(Long id);

    /**
     * Delete the "id" operator.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<Operator> findAllOperatorsByBrandName(String brandName);

    Optional<Operator> findOperatorByName(String operatorName);

    Page<OperatorDTO> findByCriteria(OperatorCriteria criteria, Pageable pageable);
}
