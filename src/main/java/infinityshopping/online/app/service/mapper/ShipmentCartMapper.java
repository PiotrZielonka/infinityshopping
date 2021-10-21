package infinityshopping.online.app.service.mapper;

import infinityshopping.online.app.domain.ShipmentCart;
import infinityshopping.online.app.service.dto.ShipmentCartDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ShipmentCartMapper extends EntityMapper<ShipmentCartDTO, ShipmentCart> {

  ShipmentCartDTO toDto(ShipmentCart s);
}
