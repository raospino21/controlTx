package co.com.ies.smol.service;

import co.com.ies.smol.domain.InterfaceBoard;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.smol.domain.InterfaceBoard}.
 */
public interface InterfaceBoardService {
    /**
     * Save a interfaceBoard.
     *
     * @param interfaceBoardDTO the entity to save.
     * @return the persisted entity.
     */
    InterfaceBoardDTO save(InterfaceBoardDTO interfaceBoardDTO);

    /**
     * Updates a interfaceBoard.
     *
     * @param interfaceBoardDTO the entity to update.
     * @return the persisted entity.
     */
    InterfaceBoardDTO update(InterfaceBoardDTO interfaceBoardDTO);

    /**
     * Partially updates a interfaceBoard.
     *
     * @param interfaceBoardDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InterfaceBoardDTO> partialUpdate(InterfaceBoardDTO interfaceBoardDTO);

    /**
     * Get all the interfaceBoards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InterfaceBoardDTO> findAll(Pageable pageable);

    /**
     * Get the "id" interfaceBoard.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InterfaceBoardDTO> findOne(Long id);

    /**
     * Delete the "id" interfaceBoard.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<InterfaceBoardDTO> getInterfaceBoardByMac(String mac);

    InterfaceBoard toEntity(InterfaceBoardDTO interfaceBoardDTO);

    List<InterfaceBoardDTO> getInterfaceBoardByReceptionOrderId(Long receptionOrderId);

    List<InterfaceBoardDTO> getInterfaceBoardByReceptionOrderIdAndIsValidated(Long receptionOrderId, Boolean isValidated);
}
