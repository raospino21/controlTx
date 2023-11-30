package co.com.ies.smol.repository;

import co.com.ies.smol.domain.Brand;
import co.com.ies.smol.domain.ControlInterfaceBoard;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Brand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BrandRepository extends JpaRepository<Brand, Long>, JpaSpecificationExecutor<Brand> {
    @Query(nativeQuery = true, value = "INSERT INTO brand(id, name) VALUES((select max(id) + 1 from brand), :name) RETURNING *")
    Brand nativeSave(@Param("name") String name);
}
