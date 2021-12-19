package infinityshopping.online.app.service.impl;

import infinityshopping.online.app.domain.ShipmentOrderMain;
import infinityshopping.online.app.repository.ShipmentOrderMainRepository;
import infinityshopping.online.app.service.ShipmentOrderMainService;
import infinityshopping.online.app.service.dto.ShipmentOrderMainDTO;
import infinityshopping.online.app.service.errors.ShipmentOrderMainNotFoundException;
import infinityshopping.online.app.service.mapper.ShipmentOrderMainMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShipmentOrderMainServiceImpl implements ShipmentOrderMainService {

  private final Logger log = LoggerFactory.getLogger(ShipmentOrderMainServiceImpl.class);

  private final ShipmentOrderMainRepository shipmentOrderMainRepository;

  private final ShipmentOrderMainMapper shipmentOrderMainMapper;

  public ShipmentOrderMainServiceImpl(
      ShipmentOrderMainRepository shipmentOrderMainRepository,
      ShipmentOrderMainMapper shipmentOrderMainMapper
  ) {
    this.shipmentOrderMainRepository = shipmentOrderMainRepository;
    this.shipmentOrderMainMapper = shipmentOrderMainMapper;
  }

  @Override
  @Transactional
  public ShipmentOrderMainDTO save(ShipmentOrderMainDTO shipmentOrderMainDto) {
    log.debug("Request to save ShipmentOrderMain : {}", shipmentOrderMainDto);
    ShipmentOrderMain shipmentOrderMain = shipmentOrderMainMapper.toEntity(shipmentOrderMainDto);
    setProperOrderMainIdToShipmentOrderMainBecauseItIsNotInDto(shipmentOrderMain);
    shipmentOrderMain = shipmentOrderMainRepository.save(shipmentOrderMain);
    return shipmentOrderMainMapper.toDto(shipmentOrderMain);
  }

  private void setProperOrderMainIdToShipmentOrderMainBecauseItIsNotInDto(
      ShipmentOrderMain shipmentOrderMain) {
    shipmentOrderMain.setOrderMain(checkIfShipmentOrderMainExist(shipmentOrderMain).getOrderMain());
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<ShipmentOrderMainDTO> findOne(Long id) {
    log.debug("Request to get ShipmentOrderMain : {}", id);
    return shipmentOrderMainRepository.findById(id).map(shipmentOrderMainMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<ShipmentOrderMainDTO> findByOrderMainId(Long id) {
    log.debug("Request to get ShipmentOrderMain by id OrderMain");
    return shipmentOrderMainRepository.findByOrderMainId(id)
        .map(shipmentOrderMainMapper::toDto);
  }

  private ShipmentOrderMain checkIfShipmentOrderMainExist(ShipmentOrderMain shipmentOrderMain) {
    return shipmentOrderMainRepository.findById(shipmentOrderMain.getId())
            .orElseThrow(ShipmentOrderMainNotFoundException::new);
  }
}
