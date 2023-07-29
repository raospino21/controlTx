package co.com.ies.smol.repository;

import co.com.ies.smol.domain.Operator;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Operator entity.
 */
@Repository
public interface OperatorRepository extends JpaRepository<Operator, Long>, JpaSpecificationExecutor<Operator> {
    default Optional<Operator> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Operator> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Operator> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct operator from Operator operator left join fetch operator.brand",
        countQuery = "select count(distinct operator) from Operator operator"
    )
    Page<Operator> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct operator from Operator operator left join fetch operator.brand")
    List<Operator> findAllWithToOneRelationships();

    @Query("select operator from Operator operator left join fetch operator.brand where operator.id =:id")
    Optional<Operator> findOneWithToOneRelationships(@Param("id") Long id);
}
