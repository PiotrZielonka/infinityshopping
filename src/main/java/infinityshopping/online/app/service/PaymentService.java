package infinityshopping.online.app.service;

import infinityshopping.online.app.service.dto.PaymentDTO;
import infinityshopping.online.app.service.dto.PaymentDtoNamePriceGross;
import java.util.List;
import java.util.Optional;

public interface PaymentService {

  PaymentDTO save(PaymentDTO paymentDto);

  List<PaymentDTO> findAll();

  List<PaymentDtoNamePriceGross> findAllNamePriceGross();

  Optional<PaymentDTO> findOne(Long id);

  void delete(Long id);
}
