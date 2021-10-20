package infinityshopping.online.app.service;

import infinityshopping.online.app.service.dto.PaymentCartDTO;
import java.util.Optional;

public interface PaymentCartService {

  PaymentCartDTO save(PaymentCartDTO paymentCartDto);

  Optional<PaymentCartDTO> findOne(Long id);

  Optional<PaymentCartDTO> findByCartId();
}
