package infinityshopping.online.app.service.mapper;

import infinityshopping.online.app.domain.Payment;
import infinityshopping.online.app.service.dto.PaymentDTO;
import infinityshopping.online.app.service.dto.PaymentDtoNamePriceGross;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {

  PaymentDtoNamePriceGross toDtoNamePriceGross(Payment s);
}
