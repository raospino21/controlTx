package co.com.ies.smol.repository;

import co.com.ies.smol.domain.DataSheetInterface;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DataSheetInterface entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataSheetInterfaceRepository
    extends JpaRepository<DataSheetInterface, Long>, JpaSpecificationExecutor<DataSheetInterface> {}
