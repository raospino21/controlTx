package co.com.ies.smol.service;

import co.com.ies.smol.service.dto.DataSheetInterfaceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.smol.domain.DataSheetInterface}.
 */
public interface DataSheetInterfaceService {
    /**
     * Save a dataSheetInterface.
     *
     * @param dataSheetInterfaceDTO the entity to save.
     * @return the persisted entity.
     */
    DataSheetInterfaceDTO save(DataSheetInterfaceDTO dataSheetInterfaceDTO);

    /**
     * Updates a dataSheetInterface.
     *
     * @param dataSheetInterfaceDTO the entity to update.
     * @return the persisted entity.
     */
    DataSheetInterfaceDTO update(DataSheetInterfaceDTO dataSheetInterfaceDTO);

    /**
     * Partially updates a dataSheetInterface.
     *
     * @param dataSheetInterfaceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DataSheetInterfaceDTO> partialUpdate(DataSheetInterfaceDTO dataSheetInterfaceDTO);

    /**
     * Get all the dataSheetInterfaces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DataSheetInterfaceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" dataSheetInterface.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DataSheetInterfaceDTO> findOne(Long id);

    /**
     * Delete the "id" dataSheetInterface.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
