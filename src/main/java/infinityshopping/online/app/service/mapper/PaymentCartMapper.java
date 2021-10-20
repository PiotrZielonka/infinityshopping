package infinityshopping.online.app.service.mapper;

import infinityshopping.online.app.domain.PaymentCart;
import infinityshopping.online.app.service.dto.PaymentCartDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CartMapper.class})
public interface PaymentCartMapper extends EntityMapper<PaymentCartDTO, PaymentCart> {

  PaymentCartDTO toDto(PaymentCart s);
}
