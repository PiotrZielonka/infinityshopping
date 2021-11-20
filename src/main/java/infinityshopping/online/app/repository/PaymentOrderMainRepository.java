package infinityshopping.online.app.repository;

import infinityshopping.online.app.domain.PaymentOrderMain;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface PaymentOrderMainRepository extends JpaRepository<PaymentOrderMain, Long> {

  Optional<PaymentOrderMain> findByOrderMainId(Long orderMainId);
}
