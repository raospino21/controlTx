package co.com.ies.smol.repository;

import co.com.ies.smol.domain.PurchaseOrder;
import co.com.ies.smol.service.dto.PurchaseOrderDTO;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PurchaseOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long>, JpaSpecificationExecutor<PurchaseOrder> {
    Optional<PurchaseOrder> getByIesOrderNumber(Long iesOrderNumber);

    @Query(
        nativeQuery = true,
        value = "INSERT INTO purchase_order (id, order_amount, create_at, ies_order_number)VALUES((select max(id) + 1 from purchase_order), :orderAmount, :createAt, :iesOrderNumber) RETURNING *"
    )
    PurchaseOrder nativeSave(
        @Param("orderAmount") Long orderAmount,
        @Param("createAt") ZonedDateTime createAt,
        @Param("iesOrderNumber") Long iesOrderNumber
    );
}
