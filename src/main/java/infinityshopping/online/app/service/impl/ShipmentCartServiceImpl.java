package infinityshopping.online.app.service.impl;

import infinityshopping.online.app.domain.ShipmentCart;
import infinityshopping.online.app.domain.User;
import infinityshopping.online.app.repository.ShipmentCartRepository;
import infinityshopping.online.app.repository.UserRepository;
import infinityshopping.online.app.security.SecurityUtils;
import infinityshopping.online.app.service.ShipmentCartService;
import infinityshopping.online.app.service.UserNotFoundException;
import infinityshopping.online.app.service.dto.ShipmentCartDTO;
import infinityshopping.online.app.service.mapper.ShipmentCartMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShipmentCartServiceImpl implements ShipmentCartService {

  private final Logger log = LoggerFactory.getLogger(ShipmentCartServiceImpl.class);

  private final ShipmentCartRepository shipmentCartRepository;

  private final UserRepository userRepository;

  private final ShipmentCartMapper shipmentCartMapper;

  private User currentLoggedUser;


  public ShipmentCartServiceImpl(ShipmentCartRepository shipmentCartRepository,
      UserRepository userRepository, ShipmentCartMapper shipmentCartMapper) {
    this.shipmentCartRepository = shipmentCartRepository;
    this.userRepository = userRepository;
    this.shipmentCartMapper = shipmentCartMapper;
  }


  @Override
  @Transactional(readOnly = true)
  public Optional<ShipmentCartDTO> findOne(Long id) {
    log.debug("Request to get ShipmentCart : {}", id);
    return shipmentCartRepository.findById(id)
        .map(shipmentCartMapper::toDto);
  }

  @Override
  @Transactional
  public ShipmentCartDTO save(ShipmentCartDTO shipmentCartDto) {
    log.debug("Request to save only update ShipmentCart : {}", shipmentCartDto);
    ShipmentCart shipmentCart = shipmentCartMapper.toEntity(shipmentCartDto);

    currentLoggedUser = checkIfUserExist();

    setCartIdToShipmentCartOfLoggedUserBecauseItIsNotInDto(shipmentCart, currentLoggedUser);

    shipmentCart = shipmentCartRepository.save(shipmentCart);
    return shipmentCartMapper.toDto(shipmentCart);
  }

  private void setCartIdToShipmentCartOfLoggedUserBecauseItIsNotInDto(
      ShipmentCart shipmentCart, User currentLoggedUser) {
    shipmentCart.setCart(currentLoggedUser.getCart());
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<ShipmentCartDTO> findByCartId() {
    log.debug("Request to get ShipmentCart of current logged user ");

    currentLoggedUser = checkIfUserExist();

    return shipmentCartRepository.findByCartId(currentLoggedUser.getCart().getId())
        .map(shipmentCartMapper::toDto);
  }

  private User checkIfUserExist() {
    return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()
            .orElseThrow(UserNotFoundException::new))
        .orElseThrow(UserNotFoundException::new);
  }
}
