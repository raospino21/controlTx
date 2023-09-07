package co.com.ies.smol.service;

import co.com.ies.smol.service.dto.ReceptionOrderDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.smol.domain.ReceptionOrder}.
 */
public interface ReceptionOrderService {
    /**
     * Save a receptionOrder.
     *
     * @param receptionOrderDTO the entity to save.
     * @return the persisted entity.
     */
    ReceptionOrderDTO save(ReceptionOrderDTO receptionOrderDTO);

    /**
     * Updates a receptionOrder.
     *
     * @param receptionOrderDTO the entity to update.
     * @return the persisted entity.
     */
    ReceptionOrderDTO update(ReceptionOrderDTO receptionOrderDTO);

    /**
     * Partially updates a receptionOrder.
     *
     * @param receptionOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReceptionOrderDTO> partialUpdate(ReceptionOrderDTO receptionOrderDTO);

    /**
     * Get all the receptionOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReceptionOrderDTO> findAll(Pageable pageable);

    /**
     * Get all the receptionOrders with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReceptionOrderDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" receptionOrder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReceptionOrderDTO> findOne(Long id);

    /**
     * Delete the "id" receptionOrder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<ReceptionOrderDTO> getReceptionOrderByIesOrderNumber(Long purchaseOrder);
}
