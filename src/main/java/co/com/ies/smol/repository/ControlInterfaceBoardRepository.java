package co.com.ies.smol.repository;

import co.com.ies.smol.domain.ControlInterfaceBoard;
import co.com.ies.smol.domain.InterfaceBoard;
import co.com.ies.smol.domain.enumeration.StatusInterfaceBoard;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ControlInterfaceBoard entity.
 */
@Repository
public interface ControlInterfaceBoardRepository
    extends JpaRepository<ControlInterfaceBoard, Long>, JpaSpecificationExecutor<ControlInterfaceBoard> {
    default Optional<ControlInterfaceBoard> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ControlInterfaceBoard> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ControlInterfaceBoard> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct controlInterfaceBoard from ControlInterfaceBoard controlInterfaceBoard left join fetch controlInterfaceBoard.contract left join fetch controlInterfaceBoard.interfaceBoard",
        countQuery = "select count(distinct controlInterfaceBoard) from ControlInterfaceBoard controlInterfaceBoard"
    )
    Page<ControlInterfaceBoard> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct controlInterfaceBoard from ControlInterfaceBoard controlInterfaceBoard left join fetch controlInterfaceBoard.contract left join fetch controlInterfaceBoard.interfaceBoard"
    )
    List<ControlInterfaceBoard> findAllWithToOneRelationships();

    @Query(
        "select controlInterfaceBoard from ControlInterfaceBoard controlInterfaceBoard left join fetch controlInterfaceBoard.contract left join fetch controlInterfaceBoard.interfaceBoard where controlInterfaceBoard.id =:id"
    )
    Optional<ControlInterfaceBoard> findOneWithToOneRelationships(@Param("id") Long id);

    Optional<ControlInterfaceBoard> getControlInterfaceBoardByInterfaceBoardAndFinishTimeIsNull(InterfaceBoard interfaceBoard);

    @Query(
        nativeQuery = true,
        value = "SELECT * FROM CONTROL_INTERFACE_BOARD WHERE id IN (SELECT id FROM CONTROL_INTERFACE_BOARD WHERE CONTRACT_ID is not null and finish_time is null and CONTRACT_ID in (?1)\n" + //
        "GROUP BY CONTRACT_ID, id)"
    )
    List<ControlInterfaceBoard> getControlInterfaceBoardByContractIds(List<Long> contractIds);

    @Query(
        "select controlInterfaceBoard from ControlInterfaceBoard controlInterfaceBoard left join fetch controlInterfaceBoard.contract left join fetch controlInterfaceBoard.interfaceBoard where controlInterfaceBoard.contract.reference =:reference and controlInterfaceBoard.finishTime is null "
    )
    List<ControlInterfaceBoard> getControlInterfaceBoardByReference(String reference);

    @Query(
        "select controlInterfaceBoard from ControlInterfaceBoard controlInterfaceBoard left join fetch controlInterfaceBoard.contract left join fetch controlInterfaceBoard.interfaceBoard where controlInterfaceBoard.contract.id =:contractId and controlInterfaceBoard.finishTime is null "
    )
    List<ControlInterfaceBoard> getByContractId(Long contractId);

    List<ControlInterfaceBoard> getByStateAndFinishTimeIsNull(StatusInterfaceBoard state);

    @Query(
        "select controlInterfaceBoard from ControlInterfaceBoard controlInterfaceBoard left join fetch controlInterfaceBoard.contract left join fetch controlInterfaceBoard.interfaceBoard where controlInterfaceBoard.contract.id =:contractId and state =:state and controlInterfaceBoard.finishTime is null "
    )
    List<ControlInterfaceBoard> getByContractIdAndState(Long contractId, StatusInterfaceBoard state);

    @Query(
        nativeQuery = true,
        value = "select * from control_interface_board cib left outer join contract c on cib.contract_id = c.id left outer join interface_board ib on cib.interface_board_id = ib.id where c.id in :contractIdList  and state =:state and (cib.finish_time is null)"
    )
    List<ControlInterfaceBoard> getByContractIdInAndState(List<Long> contractIdList, StatusInterfaceBoard state);
}
