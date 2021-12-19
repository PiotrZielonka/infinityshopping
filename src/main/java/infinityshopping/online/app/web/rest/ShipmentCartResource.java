package infinityshopping.online.app.web.rest;

import infinityshopping.online.app.domain.User;
import infinityshopping.online.app.repository.ShipmentCartRepository;
import infinityshopping.online.app.repository.UserRepository;
import infinityshopping.online.app.security.SecurityUtils;
import infinityshopping.online.app.service.ShipmentCartService;
import infinityshopping.online.app.service.errors.UserNotFoundException;
import infinityshopping.online.app.service.dto.ShipmentCartDTO;
import infinityshopping.online.app.web.rest.errors.BadRequestAlertException;
import java.net.URISyntaxException;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class ShipmentCartResource {

  private final Logger log = LoggerFactory.getLogger(ShipmentCartResource.class);

  private static final String ENTITY_NAME = "shipmentCart";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ShipmentCartService shipmentCartService;

  private final ShipmentCartRepository shipmentCartRepository;

  private final UserRepository userRepository;

  private User currentLoggedUser;

  public ShipmentCartResource(ShipmentCartService shipmentCartService,
      ShipmentCartRepository shipmentCartRepository,
      UserRepository userRepository) {
    this.shipmentCartService = shipmentCartService;
    this.shipmentCartRepository = shipmentCartRepository;
    this.userRepository = userRepository;
  }

  @PutMapping("/shipment-cart")
  public ResponseEntity<ShipmentCartDTO> updateShipmentCart(
      @Valid @RequestBody ShipmentCartDTO shipmentCartDto) throws URISyntaxException {
    log.debug("REST request to update ShipmentCart : {}", shipmentCartDto);
    if (shipmentCartDtoIdIsEqualNull(shipmentCartDto)) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (shipmentCartDtoDoesNotExistInTheDatabase(shipmentCartDto)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }
    if (shipmentCartDtoDoesNotEqualWithShipmentCartOfLogggedUserInTheDatabase(shipmentCartDto)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME,
          "doesNotBelongToProperUser");
    }

    ShipmentCartDTO result = shipmentCartService.save(shipmentCartDto);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
            shipmentCartDto.getId().toString()))
        .body(result);
  }

  @GetMapping("/shipment-cart/{id}")
  public ResponseEntity<ShipmentCartDTO> getShipmentCart(@PathVariable Long id) {
    log.debug("REST request to get ShipmentCart : {}", id);
    Optional<ShipmentCartDTO> shipmentCartDto = shipmentCartService.findOne(id);
    return ResponseUtil.wrapOrNotFound(shipmentCartDto);
  }

  @GetMapping("/shipment-cart/userShipmentCart")
  public ResponseEntity<ShipmentCartDTO> getShipmentCartOfCurrentLoggedUser() {
    log.debug("REST request to get ShipmentCart of current logged user");
    Optional<ShipmentCartDTO> shipmentCartDto = shipmentCartService.findByCartId();
    return ResponseUtil.wrapOrNotFound(shipmentCartDto);

  }

  private boolean shipmentCartDtoIdIsEqualNull(ShipmentCartDTO shipmentCartDto) {
    return (shipmentCartDto.getId() == null);
  }

  private boolean shipmentCartDtoDoesNotExistInTheDatabase(ShipmentCartDTO shipmentCartDto) {
    return (!shipmentCartRepository.existsById(shipmentCartDto.getId()));
  }

  private boolean shipmentCartDtoDoesNotEqualWithShipmentCartOfLogggedUserInTheDatabase(
      ShipmentCartDTO shipmentCartDto) {
    return (!shipmentCartDto.getId().equals(
        checkIfUserExist().getCart().getShipmentCart().getId()));
  }

  private User checkIfUserExist() {
    return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()
            .orElseThrow(UserNotFoundException::new))
        .orElseThrow(UserNotFoundException::new);
  }
}
