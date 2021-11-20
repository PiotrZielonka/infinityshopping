package infinityshopping.online.app.service;

import infinityshopping.online.app.service.dto.ShipmentOrderMainDTO;
import java.util.Optional;

public interface ShipmentOrderMainService {

  ShipmentOrderMainDTO save(ShipmentOrderMainDTO shipmentOrderMainDto);

  Optional<ShipmentOrderMainDTO> findByOrderMainId(Long id);

  Optional<ShipmentOrderMainDTO> findOne(Long id);
}
