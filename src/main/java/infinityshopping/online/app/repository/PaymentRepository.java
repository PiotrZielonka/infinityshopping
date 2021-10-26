package infinityshopping.online.app.repository;

import infinityshopping.online.app.domain.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

  Optional<Payment> findByName(String name);

}
