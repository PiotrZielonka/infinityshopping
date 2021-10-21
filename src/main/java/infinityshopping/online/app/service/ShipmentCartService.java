package infinityshopping.online.app.service;

import infinityshopping.online.app.service.dto.ShipmentCartDTO;
import java.util.Optional;

public interface ShipmentCartService {

  Optional<ShipmentCartDTO> findOne(Long id);

  ShipmentCartDTO save(ShipmentCartDTO shipmentCartDto);

  Optional<ShipmentCartDTO> findByCartId();

}
