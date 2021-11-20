package infinityshopping.online.app.service;

import infinityshopping.online.app.service.dto.PaymentOrderMainDTO;
import java.util.Optional;

public interface PaymentOrderMainService {

  PaymentOrderMainDTO save(PaymentOrderMainDTO paymentOrderMainDto);

  Optional<PaymentOrderMainDTO> findByOrderMainId(Long id);

  Optional<PaymentOrderMainDTO> findOne(Long id);
}
