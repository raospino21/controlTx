package co.com.ies.smol.repository;

import co.com.ies.smol.domain.Contract;
import co.com.ies.smol.domain.ControlInterfaceBoard;
import co.com.ies.smol.domain.InterfaceBoard;
import co.com.ies.smol.domain.enumeration.StatusInterfaceBoard;
import java.time.ZonedDateTime;
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

    @Query("SELECT controlInterfaceBoard FROM ControlInterfaceBoard controlInterfaceBoard WHERE finishTime is null and state = :state")
    Page<ControlInterfaceBoard> getByStateAndFinishTimeIsNull(Pageable pageable, @Param("state") StatusInterfaceBoard state);

    @Query(
        "SELECT controlInterfaceBoard FROM ControlInterfaceBoard controlInterfaceBoard WHERE finishTime is null and state = :state and controlInterfaceBoard.interfaceBoard.id = :interfaceBoardId"
    )
    Page<ControlInterfaceBoard> getByInterfaceBoardIdAndStateAndFinishTimeIsNull(
        Pageable pageable,
        @Param("state") StatusInterfaceBoard state,
        @Param("interfaceBoardId") Long interfaceBoardId
    );

    @Query(
        "select controlInterfaceBoard from ControlInterfaceBoard controlInterfaceBoard left join fetch controlInterfaceBoard.contract left join fetch controlInterfaceBoard.interfaceBoard where controlInterfaceBoard.contract.id =:contractId and state =:state and controlInterfaceBoard.finishTime is null "
    )
    List<ControlInterfaceBoard> getByContractIdAndState(Long contractId, @Param("state") String state);

    @Query(
        nativeQuery = true,
        value = "select cib.* from control_interface_board cib left outer join contract c on cib.contract_id = c.id left outer join interface_board ib on cib.interface_board_id = ib.id where c.id in :contractIdList and state = (:state) and (cib.finish_time is null)"
    )
    List<ControlInterfaceBoard> getByContractIdInAndState(List<Long> contractIdList, @Param("state") String state);

    @Query(
        nativeQuery = true,
        value = "select cib.* from control_interface_board cib inner join interface_board ib on ib.id = cib.interface_board_id inner join reception_order ro on ro.id = ib.reception_order_id where ib.reception_order_id = :receptionOrderId and finish_time is null"
    )
    List<ControlInterfaceBoard> getByReceptionOrderIdAndFinishTimeIsNull(@Param("receptionOrderId") Long receptionOrderId);

    @Query(
        nativeQuery = true,
        value = "select cib.* from control_interface_board cib inner join interface_board ib on ib.id = cib.interface_board_id inner join reception_order ro on ro.id = ib.reception_order_id where ib.reception_order_id in (:receptionOrderIds) and finish_time is null"
    )
    List<ControlInterfaceBoard> getByReceptionOrderIdInAndFinishTimeIsNull(@Param("receptionOrderIds") List<Long> receptionOrderIds);

    List<ControlInterfaceBoard> getByStateAndFinishTimeIsNull(StatusInterfaceBoard state);

    @Query(
        nativeQuery = true,
        value = "SELECT ctx.* FROM control_interface_board ctx WHERE interface_board_id IN (SELECT interface_board_id FROM control_interface_board GROUP BY \n" + //
        "interface_board_id having COUNT(interface_board_id) > 1) and state = :state and finish_time is null limit :limit"
    )
    List<ControlInterfaceBoard> getInterfaceBoardUsedInStock(@Param("state") String state, int limit);

    @Query(
        nativeQuery = true,
        value = "SELECT ctx.* FROM control_interface_board ctx WHERE interface_board_id IN (SELECT interface_board_id FROM control_interface_board GROUP BY \n" + //
        "interface_board_id having COUNT(interface_board_id) > 1) and state = :state and finish_time is null"
    )
    List<ControlInterfaceBoard> getInterfaceBoardUsedInStock(@Param("state") String state);

    @Query(
        nativeQuery = true,
        value = "SELECT ctx.* FROM control_interface_board ctx WHERE interface_board_id IN (SELECT interface_board_id FROM control_interface_board GROUP BY \n" + //
        "interface_board_id having COUNT(interface_board_id) = 1) and state = :state and finish_time is null limit :limit"
    )
    List<ControlInterfaceBoard> getInterfaceBoardNewInStock(@Param("state") String state, int limit);

    @Query(
        nativeQuery = true,
        value = "SELECT ctx.* FROM control_interface_board ctx WHERE interface_board_id IN (SELECT interface_board_id FROM control_interface_board GROUP BY \n" + //
        "interface_board_id having COUNT(interface_board_id) = 1) and state = :state and finish_time is null"
    )
    List<ControlInterfaceBoard> getInterfaceBoardNewInStock(@Param("state") String state);

    @Query(
        nativeQuery = true,
        value = "INSERT INTO public.control_interface_board (id, location, state, start_time, finish_time, contract_id, interface_board_id) " +
        "VALUES((select max(id) + 1 from control_interface_board), :location, :state, :startTime, :finishTime, :contractId, :interfaceBoardId) RETURNING *"
    )
    ControlInterfaceBoard nativeSave(
        String location,
        String state,
        ZonedDateTime startTime,
        ZonedDateTime finishTime,
        Long contractId,
        Long interfaceBoardId
    );
}
