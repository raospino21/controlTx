package co.com.ies.smol.service;

import co.com.ies.smol.domain.InterfaceBoard;
import co.com.ies.smol.domain.enumeration.StatusInterfaceBoard;
import co.com.ies.smol.service.dto.ControlInterfaceBoardDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.smol.domain.ControlInterfaceBoard}.
 */
public interface ControlInterfaceBoardService {
    /**
     * Save a controlInterfaceBoard.
     *
     * @param controlInterfaceBoardDTO the entity to save.
     * @return the persisted entity.
     */
    ControlInterfaceBoardDTO save(ControlInterfaceBoardDTO controlInterfaceBoardDTO);

    /**
     * Updates a controlInterfaceBoard.
     *
     * @param controlInterfaceBoardDTO the entity to update.
     * @return the persisted entity.
     */
    ControlInterfaceBoardDTO update(ControlInterfaceBoardDTO controlInterfaceBoardDTO);

    /**
     * Partially updates a controlInterfaceBoard.
     *
     * @param controlInterfaceBoardDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ControlInterfaceBoardDTO> partialUpdate(ControlInterfaceBoardDTO controlInterfaceBoardDTO);

    /**
     * Get all the controlInterfaceBoards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ControlInterfaceBoardDTO> findAll(Pageable pageable);

    /**
     * Get all the controlInterfaceBoards with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ControlInterfaceBoardDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" controlInterfaceBoard.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ControlInterfaceBoardDTO> findOne(Long id);

    /**
     * Delete the "id" controlInterfaceBoard.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<ControlInterfaceBoardDTO> getControlInterfaceBoardByInterfaceBoard(InterfaceBoard interfaceBoard);

    List<ControlInterfaceBoardDTO> getControlInterfaceBoardByContractIds(List<Long> contractIds);

    List<ControlInterfaceBoardDTO> getControlInterfaceBoardByReference(String reference);

    List<ControlInterfaceBoardDTO> getControlInterfaceBoardByContractId(Long contractId);

    List<ControlInterfaceBoardDTO> getInfoBoardsAvailable();

    List<ControlInterfaceBoardDTO> getByContractIdInAndState(List<Long> contractIdList, StatusInterfaceBoard state);

    List<ControlInterfaceBoardDTO> getByContractIdAndState(Long contractId, StatusInterfaceBoard state);
}
