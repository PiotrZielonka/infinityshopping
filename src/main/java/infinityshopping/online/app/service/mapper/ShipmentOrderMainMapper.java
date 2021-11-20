package infinityshopping.online.app.service.mapper;

import infinityshopping.online.app.domain.ShipmentOrderMain;
import infinityshopping.online.app.service.dto.ShipmentOrderMainDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {OrderMainMapper.class})
public interface ShipmentOrderMainMapper extends EntityMapper<
    ShipmentOrderMainDTO, ShipmentOrderMain> {

  ShipmentOrderMainDTO toDto(ShipmentOrderMain s);
}
