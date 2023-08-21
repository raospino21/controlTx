package co.com.ies.smol.repository;

import co.com.ies.smol.domain.PurchaseOrder;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PurchaseOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long>, JpaSpecificationExecutor<PurchaseOrder> {
    Optional<PurchaseOrder> getByIesOrderNumber(Long iesOrderNumber);
}
