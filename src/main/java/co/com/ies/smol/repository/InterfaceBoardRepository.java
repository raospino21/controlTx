package co.com.ies.smol.repository;

import co.com.ies.smol.domain.Contract;
import co.com.ies.smol.domain.InterfaceBoard;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InterfaceBoard entity.
 */
@Repository
public interface InterfaceBoardRepository extends JpaRepository<InterfaceBoard, Long>, JpaSpecificationExecutor<InterfaceBoard> {
    default Optional<InterfaceBoard> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<InterfaceBoard> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<InterfaceBoard> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct interfaceBoard from InterfaceBoard interfaceBoard left join fetch interfaceBoard.receptionOrder",
        countQuery = "select count(distinct interfaceBoard) from InterfaceBoard interfaceBoard"
    )
    Page<InterfaceBoard> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct interfaceBoard from InterfaceBoard interfaceBoard left join fetch interfaceBoard.receptionOrder")
    List<InterfaceBoard> findAllWithToOneRelationships();

    @Query(
        "select interfaceBoard from InterfaceBoard interfaceBoard left join fetch interfaceBoard.receptionOrder where interfaceBoard.id =:id"
    )
    Optional<InterfaceBoard> findOneWithToOneRelationships(@Param("id") Long id);

    Optional<InterfaceBoard> getInterfaceBoardByMac(String mac);

    List<InterfaceBoard> getByReceptionOrderId(Long receptionOrderId);

    List<InterfaceBoard> getByReceptionOrderIdAndIsValidated(Long receptionOrderId, Boolean isValidated);

    @Query(
        nativeQuery = true,
        value = "INSERT INTO interface_board (id, serial, reception_order_id, is_validated) \n" +
        "VALUES((select max(id) + 1 from interface_board), :mac, :receptionOrderId, false) RETURNING *"
    )
    InterfaceBoard nativeSave(String mac, Long receptionOrderId);
}
