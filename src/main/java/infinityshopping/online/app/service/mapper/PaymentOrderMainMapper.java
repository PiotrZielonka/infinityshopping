package infinityshopping.online.app.service.mapper;

import infinityshopping.online.app.domain.PaymentOrderMain;
import infinityshopping.online.app.service.dto.PaymentOrderMainDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {OrderMainMapper.class})
public interface PaymentOrderMainMapper extends EntityMapper<
    PaymentOrderMainDTO, PaymentOrderMain> {

  PaymentOrderMainDTO toDto(PaymentOrderMain s);
}
