package co.com.ies.smol.repository;

import co.com.ies.smol.domain.ReceptionOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReceptionOrder entity.
 */
@Repository
public interface ReceptionOrderRepository extends JpaRepository<ReceptionOrder, Long>, JpaSpecificationExecutor<ReceptionOrder> {
    default Optional<ReceptionOrder> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ReceptionOrder> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ReceptionOrder> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct receptionOrder from ReceptionOrder receptionOrder left join fetch receptionOrder.purchaseOrder",
        countQuery = "select count(distinct receptionOrder) from ReceptionOrder receptionOrder"
    )
    Page<ReceptionOrder> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct receptionOrder from ReceptionOrder receptionOrder left join fetch receptionOrder.purchaseOrder")
    List<ReceptionOrder> findAllWithToOneRelationships();

    @Query(
        "select receptionOrder from ReceptionOrder receptionOrder left join fetch receptionOrder.purchaseOrder where receptionOrder.id =:id"
    )
    Optional<ReceptionOrder> findOneWithToOneRelationships(@Param("id") Long id);
}
