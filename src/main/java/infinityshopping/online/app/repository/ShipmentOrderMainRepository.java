package infinityshopping.online.app.repository;

import infinityshopping.online.app.domain.ShipmentOrderMain;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface ShipmentOrderMainRepository extends JpaRepository<ShipmentOrderMain, Long> {

  Optional<ShipmentOrderMain> findByOrderMainId(Long orderMainId);
}
