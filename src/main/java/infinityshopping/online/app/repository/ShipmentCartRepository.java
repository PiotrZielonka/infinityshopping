package infinityshopping.online.app.repository;

import infinityshopping.online.app.domain.ShipmentCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface ShipmentCartRepository extends JpaRepository<ShipmentCart, Long> {

  Optional<ShipmentCart> findByCartId(Long cartId);
}
