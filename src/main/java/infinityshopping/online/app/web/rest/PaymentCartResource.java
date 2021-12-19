package infinityshopping.online.app.web.rest;

import infinityshopping.online.app.domain.User;
import infinityshopping.online.app.repository.PaymentCartRepository;
import infinityshopping.online.app.repository.UserRepository;
import infinityshopping.online.app.security.SecurityUtils;
import infinityshopping.online.app.service.PaymentCartService;
import infinityshopping.online.app.service.UserNotFoundException;
import infinityshopping.online.app.service.dto.PaymentCartDTO;
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
public class PaymentCartResource {

  private final Logger log = LoggerFactory.getLogger(PaymentCartResource.class);

  private static final String ENTITY_NAME = "paymentCart";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final PaymentCartService paymentCartService;

  private final PaymentCartRepository paymentCartRepository;

  private final UserRepository userRepository;

  private User currentLoggedUser;

  public PaymentCartResource(
      PaymentCartService paymentCartService,
      PaymentCartRepository paymentCartRepository,
      UserRepository userRepository) {
    this.paymentCartService = paymentCartService;
    this.paymentCartRepository = paymentCartRepository;
    this.userRepository = userRepository;
  }

  @PutMapping("/payment-cart")
  public ResponseEntity<PaymentCartDTO> updatePaymentCart(
      @Valid @RequestBody PaymentCartDTO paymentCartDto) throws URISyntaxException {
    log.debug("REST request to update PaymentCart : {}", paymentCartDto);
    if (paymentCartDtoIdIsEqualNull(paymentCartDto)) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (paymentCartDtoDoesNotExistInTheDatabase(paymentCartDto)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }
    if (paymentCartDtoDoesNotEqualWithPaymentCartOfLogggedUserInTheDatabase(paymentCartDto)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME,
          "doesNotBelongToProperUser");
    }
    PaymentCartDTO result = paymentCartService.save(paymentCartDto);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(
            applicationName, true, ENTITY_NAME, paymentCartDto.getId().toString()))
        .body(result);
  }

  @GetMapping("/payment-cart/{id}")
  public ResponseEntity<PaymentCartDTO> getPaymentCart(@PathVariable Long id) {
    log.debug("REST request to get PaymentCart : {}", id);
    Optional<PaymentCartDTO> paymentCartDto = paymentCartService.findOne(id);
    return ResponseUtil.wrapOrNotFound(paymentCartDto);
  }

  @GetMapping("/payment-cart/userPaymentCart")
  public ResponseEntity<PaymentCartDTO> getPaymentCartOfCurrentLoggedUser() {
    log.debug("REST request to get PaymentCart of current logged user");
    Optional<PaymentCartDTO> paymentCartDto = paymentCartService.findByCartId();
    return ResponseUtil.wrapOrNotFound(paymentCartDto);
  }

  private boolean paymentCartDtoIdIsEqualNull(PaymentCartDTO paymentCartDto) {
    return (paymentCartDto.getId() == null);
  }

  private boolean paymentCartDtoDoesNotExistInTheDatabase(PaymentCartDTO paymentCartDto) {
    return (!paymentCartRepository.existsById(paymentCartDto.getId()));
  }

  private boolean paymentCartDtoDoesNotEqualWithPaymentCartOfLogggedUserInTheDatabase(
      PaymentCartDTO paymentCartDto) {
    return (!paymentCartDto.getId().equals(checkIfUserExist().getCart().getPaymentCart().getId()));
  }

  private User checkIfUserExist() {
    return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()
            .orElseThrow(UserNotFoundException::new))
        .orElseThrow(UserNotFoundException::new);
  }
}
