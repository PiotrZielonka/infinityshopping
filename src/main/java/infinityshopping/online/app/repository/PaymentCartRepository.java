package infinityshopping.online.app.repository;

import infinityshopping.online.app.domain.PaymentCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface PaymentCartRepository extends JpaRepository<PaymentCart, Long> {

  Optional<PaymentCart> findByCartId(Long cartId);
}
