package co.com.ies.smol.repository;

import co.com.ies.smol.domain.InterfaceBoard;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InterfaceBoard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InterfaceBoardRepository extends JpaRepository<InterfaceBoard, Long>, JpaSpecificationExecutor<InterfaceBoard> {}
